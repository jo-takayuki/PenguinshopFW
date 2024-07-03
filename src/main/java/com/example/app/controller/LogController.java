package com.example.app.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.app.domain.Company;
import com.example.app.domain.LoginForm;
import com.example.app.domain.Member;
import com.example.app.service.CompanyService;
import com.example.app.service.MemberService;

@Controller("/")
public class LogController {

	@Autowired
	private MemberService mService;

	@Autowired
	private CompanyService cService;

	@GetMapping
	public String login(Model model) {
		model.addAttribute("title", "ログイン");
		model.addAttribute("loginForm", new LoginForm());
		return "login";
	}

	@PostMapping
	public String loginPost(@Valid LoginForm loginForm, Errors errors, HttpSession session) throws Exception {
		if (errors.hasErrors()) {
			return "login";
		}
		if (!mService.findByEmailAndPass(loginForm.getEmail(), loginForm.getPass())) {
			if (!cService.findByEmailAndPass(loginForm.getEmail(), loginForm.getPass())) {
				errors.rejectValue("email", "error.find_id_pass");
				return "login";
			}
		}
		Member member = mService.selectEmail(loginForm.getEmail());
		Company company = cService.selectEmail(loginForm.getEmail());
		if (member != null) {
			session.setAttribute("member", member);
			System.out.println("mem:" + member);
			return "redirect:/indiv";
		} else {
			if (company != null) {
				session.setAttribute("company", company);
				System.out.println("com:" + company);
				return "redirect:/corp";
			}
		}
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session, Model model) {
		session.invalidate();
		return "redirect:/";
	}

}

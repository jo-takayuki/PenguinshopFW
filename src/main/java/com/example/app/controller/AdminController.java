package com.example.app.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Admin;
import com.example.app.service.AdminService;
import com.example.app.service.CompanyService;
import com.example.app.service.HistoryService;
import com.example.app.service.ItemService;
import com.example.app.service.MemberService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private static final int NUM_PAGE = 10;

	@Autowired
	private AdminService aService;

	@Autowired
	private MemberService mService;

	@Autowired
	private CompanyService cService;

	@Autowired
	private ItemService iService;

	@Autowired
	private HistoryService hService;

	@GetMapping
	public String admin(Model model) {
		model.addAttribute("admin", new Admin());
		model.addAttribute("title", "管理者ページ");
		return "admin/admin";
	}

	@PostMapping
	public String adminPost(@Valid Admin admin, Errors errors, HttpSession session) throws Exception {
		if (errors.hasErrors()) {
			return "admin/admin";
		}
		if (!aService.select(admin.getLoginId(), admin.getPass())) {
			errors.rejectValue("loginId", "error.find_loginId_pass");
			return "admin/admin";
		}
		session.setAttribute("loginId", admin.getLoginId());
		return "redirect:/admin";
	}

	@GetMapping("/member")
	public String member(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) throws Exception {
		model.addAttribute("title", "管理者ページ");
		model.addAttribute("member", mService.limitMember(page, NUM_PAGE));
		model.addAttribute("page", page);
		model.addAttribute("totalPage", mService.totalPage(NUM_PAGE));
		return "admin/admin";
	}

	@GetMapping("/company")
	public String conpany(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) throws Exception {
		model.addAttribute("title", "管理者ページ");
		model.addAttribute("company", cService.limitCompany(page, NUM_PAGE));
		model.addAttribute("page", page);
		model.addAttribute("totalPage", cService.totalPage(NUM_PAGE));
		return "admin/admin";
	}

	@GetMapping("/item")
	public String item(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) throws Exception {
		model.addAttribute("title", "管理者ページ");
		model.addAttribute("item", iService.limitItem(page, NUM_PAGE));
		model.addAttribute("page", page);
		model.addAttribute("totalPage", iService.totalPage(NUM_PAGE));
		return "admin/admin";
	}

	@GetMapping("/history")
	public String history(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) throws Exception {
		model.addAttribute("title", "管理者ページ");
		model.addAttribute("history", hService.limitHistory(page, NUM_PAGE));
		model.addAttribute("page", page);
		model.addAttribute("totalPage", hService.totalPage(NUM_PAGE));
		return "admin/admin";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/admin";
	}

}

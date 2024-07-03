package com.example.app.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Cart;
import com.example.app.domain.InsertGroup;
import com.example.app.domain.Member;
import com.example.app.service.CartService;
import com.example.app.service.HistoryService;
import com.example.app.service.ItemService;
import com.example.app.service.MailService;
import com.example.app.service.MemberService;

@Controller
@RequestMapping("/indiv")
public class MemberController {

	private static final int NUM_PAGE = 10;

	@Autowired
	private MemberService mService;

	@Autowired
	private ItemService iService;

	@Autowired
	private HistoryService hService;

	@Autowired
	private CartService cService;

	@Autowired
	private MailService mailService;

	@GetMapping
	public String home(@RequestParam(name = "page", defaultValue = "1") Integer page, Model model) throws Exception {
		model.addAttribute("title", "ホーム");
		model.addAttribute("items", iService.limitItem(page, NUM_PAGE));
		model.addAttribute("page", page);
		model.addAttribute("totalPage", iService.totalPage(NUM_PAGE));
		return "member/home";
	}

	@GetMapping("/search")
	public String search(@RequestParam String key, Model model) throws Exception {
		model.addAttribute("title", "検索：" + key);
		model.addAttribute("items", iService.search(key));
		return "member/home";
	}

	@GetMapping("/account")
	public String accountGet(Model model) throws Exception {
		model.addAttribute("member", new Member());
		model.addAttribute("title", "新規登録");
		model.addAttribute("types", mService.getAll());
		return "member/account";
	}

	@PostMapping("/account")
	public String accountPost(@Validated(InsertGroup.class) Member member, Errors errors, Model model)
			throws Exception {
		if (!member.getPass().equals(member.getPassConf())) {
			errors.rejectValue("passConf", "error.pass");
		}
		if (errors.hasErrors()) {
			model.addAttribute("title", "新規登録");
			model.addAttribute("types", mService.getAll());
			return "member/account";
		}
		if (mService.selectEmail(member.getEmail()) != null) {
			if (member.getEmail().equals(mService.selectEmail(member.getEmail()).getEmail())) {
				return "arranged";
			}
		}
		String Password = member.getPass();
		mService.insert(member);
		member.setPass(Password);
		mailService.memberEmail(member);
		return "redirect:/indiv/accountDone";
	}

	@GetMapping("/accountDone")
	public String accountDone(Model model) {
		model.addAttribute("title", "登録完了");
		return "accountDone";
	}

	@GetMapping("/item/{id}")
	public String itemGet(@PathVariable Integer id, HttpSession session, Model model) throws Exception {
		Cart cart = null;
		if (session.getAttribute("cartList") != null) {
			cart = cService.cartItem(id, session);
		}
		if (cart != null) {
			model.addAttribute("cart", cart);
		} else {
			model.addAttribute("cart", new Cart());
		}
		model.addAttribute("title", iService.findById(id).getName());
		model.addAttribute("item", iService.findById(id));
		model.addAttribute("total", cService.cartTotal(id, session));
		model.addAttribute("pictureList", iService.pictureAll(id));
		return "member/item";
	}

	@PostMapping("/item/{id}")
	public String itemPost(@PathVariable Integer id, @Valid Cart cart, Errors errors, HttpSession session, Model model)
			throws Exception {
		if (errors.hasErrors()) {
			model.addAttribute("title", iService.findById(id).getName());
			model.addAttribute("item", iService.findById(id));
			model.addAttribute("pictureList", iService.pictureAll(id));
			return "member/item";
		}
		cService.cartList(id, cart.getUnit(), session);
		model.addAttribute("title", iService.findById(id).getName());
		model.addAttribute("item", iService.findById(id));
		model.addAttribute("total", cService.cartTotal(id, session));
		model.addAttribute("pictureList", iService.pictureAll(id));
		return "member/item";
	}

	@GetMapping("/deleteCartList/{id}")
	public String deleteCartList(@PathVariable Integer id, HttpSession session) throws Exception {
		cService.deleteCartList(id, session);
		return "redirect:/indiv/cart";
	}

	@GetMapping("/myPage")
	public String myPage(HttpSession session, Model model) throws Exception {
		model.addAttribute("title", "マイページ");
		model.addAttribute("items", hService.historyList(session));
		return "member/myPage";
	}

	@GetMapping("/issue")
	public String issue(HttpSession session, Model model) throws Exception {
		model.addAttribute("title", "レシートの発行");
		if (hService.seles(session) != null && hService.seles(session).size() > 0) {
			model.addAttribute("issueList", hService.seles(session));
		}
		return "member/issue";
	}

	@GetMapping("/cart")
	@SuppressWarnings("unchecked")
	public String cart(HttpSession session, Model model) throws Exception {
		if (session.getAttribute("cartList") != null && ((List<Cart>) session.getAttribute("cartList")).size() > 0) {
			model.addAttribute("items", session.getAttribute("cartList"));
		}
		model.addAttribute("amount", cService.cartAmount(session));
		model.addAttribute("title", "カート");
		return "member/cart";
	}

	@PostMapping("/cart")
	public String cartPost(HttpSession session, Model model) throws Exception {
		hService.insert(session);
		model.addAttribute("title", "ご注文ありがとうございます");
		session.removeAttribute("cartList");
		return "member/cartDone";
	}

	@GetMapping("/expl")
	public String expl(Model model) {
		model.addAttribute("title", "概要ページ");
		return "member/expl";
	}

	@GetMapping("/update")
	public String update(HttpSession session, Model model) throws Exception {
		model.addAttribute("title", "個人情報の変更");
		model.addAttribute("member", session.getAttribute("member"));
		model.addAttribute("types", mService.getAll());
		return "member/update";
	}

	@PostMapping("/update")
	public String updatePost(@Valid Member member, Errors errors, Model model) throws Exception {
		mService.update(member);
		return "redirect:updateDone";
	}

	@GetMapping("/updateDone")
	public String updateDone(Model model) {
		model.addAttribute("title", "登録完了");
		return "member/updateDone";
	}

	@GetMapping("/delete")
	public String delete(Model model) {
		model.addAttribute("title", "削除");
		return "member/delete";
	}

	@PostMapping("/delete")
	public String deletePost(HttpSession session, Model model) throws Exception {
		Member member = (Member) session.getAttribute("member");
		mService.delete(member.getId());
		return "redirect:deleteDone";
	}

	@GetMapping("/deleteDone")
	public String deleteDone(Model model) {
		model.addAttribute("title", "削除しました");
		return "deleteDone";
	}

	@GetMapping("/receipt/{created}")
	public String receipt(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date created, HttpSession session,
			Model model) throws Exception {
		model.addAttribute("title", "レシート");
		model.addAttribute("items", hService.receipt(session, created).getReceipts());
		model.addAttribute("total", hService.receipt(session, created).getTotal());
		model.addAttribute("created", created);
		return "member/receipt";
	}

}

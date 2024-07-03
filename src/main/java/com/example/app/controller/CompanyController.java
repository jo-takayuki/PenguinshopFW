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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Cart;
import com.example.app.domain.Company;
import com.example.app.domain.InsertGroup;
import com.example.app.domain.Item;
import com.example.app.service.CartService;
import com.example.app.service.CompanyService;
import com.example.app.service.HistoryService;
import com.example.app.service.ItemService;
import com.example.app.service.MailService;

@Controller
@RequestMapping("/corp")
public class CompanyController {

	private static final int NUM_PAGE = 10;

	@Autowired
	private CompanyService mService;

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
		return "company/home";
	}

	@GetMapping("/search")
	public String search(@RequestParam String key, Model model) throws Exception {
		model.addAttribute("title", "検索：" + key);
		model.addAttribute("items", iService.search(key));
		return "company/home";
	}

	@GetMapping("/account")
	public String accountGet(Model model) throws Exception {
		model.addAttribute("company", new Company());
		model.addAttribute("title", "新規登録");
		return "company/account";
	}

	@PostMapping("/account")
	public String accountPost(@Validated(InsertGroup.class) Company company, Errors errors, Model model)
			throws Exception {
		if (!company.getPass().equals(company.getPassConf())) {
			errors.rejectValue("passConf", "error.pass");
		}
		if (errors.hasErrors()) {
			model.addAttribute("title", "新規登録");
			return "company/account";
		}
		if (mService.selectEmail(company.getEmail()) != null) {
			if (company.getEmail().equals(mService.selectEmail(company.getEmail()).getEmail())) {
				return "arranged";
			}
		}
		String password = company.getPass();
		mService.insert(company);
		company.setPass(password);
		mailService.companyEmail(company);
		return "redirect:/corp/accountDone";
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
		return "company/item";
	}

	@PostMapping("/item/{id}")
	public String itemPost(@PathVariable Integer id, @Valid Cart cart, Errors errors, HttpSession session, Model model)
			throws Exception {
		if (errors.hasErrors()) {
			model.addAttribute("title", iService.findById(id).getName());
			model.addAttribute("item", iService.findById(id));
			model.addAttribute("pictureList", iService.pictureAll(id));
			return "company/item";
		}
		cService.cartList(id, cart.getUnit(), session);
		model.addAttribute("title", iService.findById(id).getName());
		model.addAttribute("item", iService.findById(id));
		model.addAttribute("total", cService.cartTotal(id, session));
		model.addAttribute("pictureList", iService.pictureAll(id));
		return "company/item";
	}

	@GetMapping("/add")
	public String add(HttpSession session, Model model) throws Exception {
		Company company = (Company) session.getAttribute("company");
		model.addAttribute("title", "商品登録");
		model.addAttribute("item", new Item());
		model.addAttribute("items", iService.findByCompanyId(company.getId()));
		return "company/add";
	}

	@PostMapping("/add")
	public String addPost(HttpSession session, @Valid Item item, Errors errors, RedirectAttributes ra, Model model)
			throws Exception {
		Company company = (Company) session.getAttribute("company");
		List<MultipartFile> imageFiles = item.getImageFiles();
		item.setCompanyId(company.getId());
		for (MultipartFile pic : imageFiles) {
			if (!imageFiles.isEmpty()) {
				String type = pic.getContentType();
				if (!type.startsWith("image/")) {
					errors.rejectValue("picture", "error.file");
				}
			}
		}
		if (errors.hasErrors()) {
			model.addAttribute("title", "商品登録");
			model.addAttribute("items", iService.findByCompanyId(company.getId()));
			System.out.println("未登録");
			return "company/add";
		}
		iService.insert(item);
		ra.addFlashAttribute("message", "商品を追加しました。");
		return "redirect:/corp/add";
	}

	@GetMapping("/deleteItem/{id}")
	public String deleteItem(@PathVariable Integer id, RedirectAttributes ra) throws Exception {
		iService.delete(id);
		ra.addAttribute("message", "削除しました。");
		return "redirect:/corp/add";
	}

	@GetMapping("/updateItem/{id}")
	public String updateItem(@PathVariable Integer id, Model model) throws Exception {
		model.addAttribute("title", iService.findById(id).getName());
		model.addAttribute("item", iService.findById(id));
		model.addAttribute("pictureList", iService.pictureAll(id));
		return "company/updateItem";
	}

	@PostMapping("/updateItem/{id}")
	public String updateItemPost(HttpSession session, @Valid Item item, Errors errors, @PathVariable Integer id,
			RedirectAttributes ra, Model model) throws Exception {
		Company company = (Company) session.getAttribute("company");
		List<MultipartFile> imageFiles = item.getImageFiles();
		item.setCompanyId(company.getId());
		for (MultipartFile pic : imageFiles) {
			if (!imageFiles.isEmpty()) {
				String type = pic.getContentType();
				if (!type.startsWith("image/")) {
					errors.rejectValue("picture", "error.file");
				}
			}
		}
		if (errors.hasErrors()) {
			model.addAttribute("title", "商品登録");
			model.addAttribute("items", iService.findByCompanyId(company.getId()));
			System.out.println("未登録");
			return "company/add";
		}
		iService.update(item);
		ra.addFlashAttribute("message", "商品を変更しました。");
		return "redirect:/corp/add";
	}

	@GetMapping("/deleteCartList/{id}")
	public String deleteCartList(@PathVariable Integer id, HttpSession session) throws Exception {
		cService.deleteCartList(id, session);
		return "redirect:/corp/cart";
	}

	@GetMapping("/myPage")
	public String myPage(HttpSession session, Model model) throws Exception {
		model.addAttribute("title", "マイページ");
		model.addAttribute("items", hService.historyList(session));
		return "company/myPage";
	}

	@GetMapping("/issue")
	public String issue(HttpSession session, Model model) throws Exception {
		model.addAttribute("title", "レシートの発行");
		if (hService.seles(session) != null && hService.seles(session).size() > 0) {
			model.addAttribute("issueList", hService.seles(session));
		}
		return "company/issue";
	}

	@GetMapping("/cart")
	@SuppressWarnings("unchecked")
	public String cart(HttpSession session, Model model) throws Exception {
		if (session.getAttribute("cartList") != null && ((List<Cart>) session.getAttribute("cartList")).size() > 0) {
			model.addAttribute("items", session.getAttribute("cartList"));
		}
		model.addAttribute("amount", cService.cartAmount(session));
		model.addAttribute("title", "カート");
		return "company/cart";
	}

	@PostMapping("/cart")
	public String cartPost(HttpSession session, Model model) throws Exception {
		hService.insert(session);
		model.addAttribute("title", "ご注文ありがとうございます");
		session.removeAttribute("cartList");
		return "company/cartDone";
	}

	@GetMapping("/expl")
	public String expl(Model model) {
		model.addAttribute("title", "概要ページ");
		return "company/expl";
	}

	@GetMapping("/update")
	public String update(HttpSession session, Model model) throws Exception {
		model.addAttribute("title", "個人情報の変更");
		model.addAttribute("company", session.getAttribute("company"));
		return "company/update";
	}

	@PostMapping("/update")
	public String updatePost(@Valid Company company, Errors errors, Model model) throws Exception {
		mService.update(company);
		return "redirect:updateDone";
	}

	@GetMapping("/updateDone")
	public String updateDone(Model model) {
		model.addAttribute("title", "登録完了");
		return "company/updateDone";
	}

	@GetMapping("/delete")
	public String delete(Model model) {
		model.addAttribute("title", "削除");
		return "company/delete";
	}

	@PostMapping("/delete")
	public String deletePost(HttpSession session, Model model) throws Exception {
		Company company = (Company) session.getAttribute("company");
		mService.delete(company.getId());
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
		return "company/receipt";
	}

}

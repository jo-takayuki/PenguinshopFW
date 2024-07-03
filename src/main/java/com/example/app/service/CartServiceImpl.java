package com.example.app.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Cart;
import com.example.app.domain.Item;

@Service
@Transactional(rollbackFor = Exception.class)
public class CartServiceImpl implements CartService {

	@Autowired
	private ItemService iService;

	@Override
	public Cart cartAdd(Integer id, Integer unit) throws Exception {
		Cart cart = new Cart();
		Item item = iService.findCart(id);
		cart.setId(item.getId());
		cart.setName(item.getName());
		cart.setPrice(item.getPrice());
		cart.setText(item.getText());
		cart.setCompanyId(item.getCompanyId());
		cart.setUnit(unit);
		cart.setCreated(item.getCreated());
		cart.setPictureName(item.getPictureName());
		System.out.println("CSI:" + cart);
		return cart;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer cartTotal(Integer id, HttpSession session) throws Exception {
		Integer total = 0;
		if (session.getAttribute("cartList") != null) {
			List<Cart> cartList = (List<Cart>) session.getAttribute("cartList");
			for (Cart cart : cartList) {
				if (id == cart.getId()) {
					total = cart.getUnit() * cart.getPrice();
				}
			}
		}
		return total;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Integer cartAmount(HttpSession session) throws Exception {
		List<Cart> cartList = (List<Cart>) session.getAttribute("cartList");
		int amount = 0;
		if (session.getAttribute("cartList") != null) {
			for (Cart cart : cartList) {
				int total = cartTotal(cart.getId(), session);
				amount += total;
			}
		}
		return amount;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Cart cartItem(Integer id, HttpSession session) throws Exception {
		List<Cart> cartList = (List<Cart>) session.getAttribute("cartList");
		Cart cart = null;
		for (Cart cart1 : cartList) {
			if (id == cart1.getId()) {
				cart = cart1;
			}
		}
		return cart;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cart> cartList(Integer id, Integer unit, HttpSession session) throws Exception {
		List<Cart> cartList;
		if (session.getAttribute("cartList") != null) {
			cartList = (List<Cart>) session.getAttribute("cartList");
			List<Cart> updatedCartList = new ArrayList<>();
			boolean updated = false;
			for (Cart cart : cartList) {
				if (cart.getId() == id) {
					cart.setUnit(unit);
					updated = true;// 同じ商品がカート内に存在する場合、個数を更新
				}
				updatedCartList.add(cart);
			}
			if (!updated) {
				updatedCartList.add(cartAdd(id, unit));// 同じ商品がカート内に存在しない場合、新しい商品を追加
			}
			session.setAttribute("cartList", updatedCartList); // セッションに更新後のリストを設定
		} else {
			cartList = new ArrayList<>();
			cartList.add(cartAdd(id, unit));
			session.setAttribute("cartList", cartList); // セッションに新しいリストを設定
		}
		return cartList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Cart> deleteCartList(Integer id, HttpSession session) throws Exception {
		List<Cart> cartList = (List<Cart>) session.getAttribute("cartList");
		List<Cart> updateCartList = new ArrayList<>();
		for (Cart cart : cartList) {
			if (cart.getId() != id) {
				updateCartList.add(cart);
			}
		}
		session.setAttribute("cartList", updateCartList);
		return updateCartList;
	}

}

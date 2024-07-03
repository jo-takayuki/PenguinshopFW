package com.example.app.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.example.app.domain.Cart;

public interface CartService {

	Cart cartAdd(Integer id, Integer unit) throws Exception;

	Integer cartTotal(Integer id, HttpSession session) throws Exception;

	Integer cartAmount(HttpSession session) throws Exception;

	Cart cartItem(Integer id, HttpSession session) throws Exception;

	List<Cart> cartList(Integer id, Integer unit, HttpSession session) throws Exception;

	List<Cart> deleteCartList(Integer id, HttpSession session) throws Exception;

}

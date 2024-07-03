package com.example.app.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthFilterCompany extends HttpFilter{
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String uri = req.getRequestURI();
		if (!uri.endsWith("/") && !uri.endsWith("/corp/account") && !uri.endsWith("/corp/accountDone")) {
			if (session.getAttribute("company") == null) {
				res.sendRedirect("/penguinshop");
				return;
			}
		}
		chain.doFilter(request, response);
	}

}

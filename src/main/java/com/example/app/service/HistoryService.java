package com.example.app.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.example.app.domain.History;
import com.example.app.domain.Item;
import com.example.app.domain.Total;

public interface HistoryService {

	List<History> findAll() throws Exception;

	List<History> findByIdAndTid(Integer id, Integer tId) throws Exception;

	List<Item> historyList(HttpSession session) throws Exception;

	void insert(HttpSession session) throws Exception;

	int totalPage(int numPage) throws Exception;

	List<History> limitHistory(int page, int numPage) throws Exception;

	List<History> seles(HttpSession session) throws Exception;
	
	Total receipt(HttpSession session, Date created) throws Exception;

}

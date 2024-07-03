package com.example.app.service;

import java.util.List;

import com.example.app.domain.Item;
import com.example.app.domain.Picture;

public interface ItemService {

	List<Item> findAll() throws Exception;

	Item findById(Integer id) throws Exception;

	List<Picture> pictureAll(Integer Id) throws Exception;

	List<Item> search(String key) throws Exception;

	int totalPage(int numPage) throws Exception;

	List<Item> limitItem(int page, int numPage) throws Exception;

	void insert(Item item) throws Exception;

	void update(Item item) throws Exception;

	void delete(Integer id) throws Exception;
	
	List<Item> findByCompanyId(Integer id) throws Exception;

	Item findCart(Integer id) throws Exception;

}

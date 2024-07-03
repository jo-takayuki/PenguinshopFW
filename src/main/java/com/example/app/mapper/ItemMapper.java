package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Item;

public interface ItemMapper {

	List<Item> findAll() throws Exception;

	Item findById(Integer id) throws Exception;

	List<Item> search(String key) throws Exception;

	Long count() throws Exception;

	List<Item> limit(@Param("offset") int offset, @Param("numPage") int numPage) throws Exception;
	
	void insert(Item item) throws Exception;
	
	void update(Item item) throws Exception;
	
	void delete(Integer id) throws Exception;
	
	List<Item> findByCompanyId(Integer id) throws Exception;

	Item findCart(Integer id) throws Exception;
}

package com.example.app.mapper;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;

import com.example.app.domain.History;
import com.example.app.domain.Item;

public interface HistoryMapper {

	List<History> findAll() throws Exception;

	List<History> findByIdAndTid(Integer id, Integer tId) throws Exception;

	List<Item> historyList(HttpSession session) throws Exception;

	void insert(History history) throws Exception;

	Long count() throws Exception;

	List<History> limit(@Param("offset") int offset, @Param("numPage") int numPage) throws Exception;

	List<History> seles(@Param("id") Integer id, @Param("tId") Integer tId) throws Exception;

}

package com.example.app.service;

import java.util.List;

import com.example.app.domain.Gender;
import com.example.app.domain.Member;

public interface MemberService {

	List<Member> findAll() throws Exception;

	boolean findByEmailAndPass(String email, String pass) throws Exception;

	void insert(Member member) throws Exception;

	void update(Member member) throws Exception;

	void delete(Integer id) throws Exception;

	List<Gender> getAll() throws Exception;

	Member selectEmail(String email) throws Exception;

	int totalPage(int numPage) throws Exception;

	List<Member> limitMember(int page, int numPage) throws Exception;

}

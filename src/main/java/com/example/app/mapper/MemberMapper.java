package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Member;

public interface MemberMapper {

	List<Member> findAll() throws Exception;

	Member findByEmail(String email) throws Exception;

	void insert(Member member) throws Exception;

	void update(Member member) throws Exception;

	void delete(Integer id) throws Exception;

	Long count() throws Exception;

	List<Member> limit(@Param("offset") int offset, @Param("numPage") int numPage) throws Exception;

}

package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Company;

public interface CompanyMapper {

	List<Company> findAll() throws Exception;

	Company findByEmail(String email) throws Exception;

	void insert(Company company) throws Exception;

	void update(Company company) throws Exception;

	void delete(Integer id) throws Exception;

	Long count() throws Exception;

	List<Company> limit(@Param("offset") int offset, @Param("numPage") int numPage) throws Exception;

}

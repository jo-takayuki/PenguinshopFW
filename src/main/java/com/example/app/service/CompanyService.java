package com.example.app.service;

import java.util.List;

import com.example.app.domain.Company;

public interface CompanyService {

	List<Company> findAll() throws Exception;

	boolean findByEmailAndPass(String email, String pass) throws Exception;

	void insert(Company company) throws Exception;

	void update(Company company) throws Exception;

	void delete(Integer id) throws Exception;

	int totalPage(int numPage) throws Exception;

	List<Company> limitCompany(int page, int numPage) throws Exception;

	Company selectEmail(String email) throws Exception;

}

package com.example.app.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Company;
import com.example.app.mapper.CompanyMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyMapper mapper;

	@Override
	public List<Company> findAll() throws Exception {
		return mapper.findAll();
	}

	@Override
	public boolean findByEmailAndPass(String email, String pass) throws Exception {
		Company company = mapper.findByEmail(email);
		if (company == null) {
			return false;
		}
		if (!BCrypt.checkpw(pass, company.getPass())) {
			return false;
		}
		return true;
	}

	@Override
	public void insert(Company company) throws Exception {
		String passH = BCrypt.hashpw(company.getPass(), BCrypt.gensalt());
		company.setPass(passH);
		mapper.insert(company);
	}

	@Override
	public void update(Company company) throws Exception {
		String passH = "";
		if (!company.getPass().isBlank()) {
			passH = BCrypt.hashpw(company.getPass(), BCrypt.gensalt());
		}
		company.setPass(passH);
		mapper.update(company);
	}

	@Override
	public void delete(Integer id) throws Exception {
		mapper.delete(id);
	}

	@Override
	public int totalPage(int numPage) throws Exception {
		double totalNum = mapper.count();
		return (int) Math.ceil(totalNum / numPage);
	}

	@Override
	public List<Company> limitCompany(int page, int numPage) throws Exception {
		int offset = numPage * (page - 1);
		return mapper.limit(offset, numPage);
	}

	@Override
	public Company selectEmail(String email) throws Exception {
		return mapper.findByEmail(email);
	}

}

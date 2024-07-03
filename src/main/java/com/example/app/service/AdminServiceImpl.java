package com.example.app.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Admin;
import com.example.app.mapper.AdminMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminMapper mapper;

	@Override
	public boolean select(String loginId, String pass) throws Exception {
		Admin admin = mapper.select(loginId);
		if (admin == null) {
			return false;
		}
		if (!BCrypt.checkpw(pass, admin.getPass())) {
			return false;
		}
		return true;
	}

}

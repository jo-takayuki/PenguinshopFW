package com.example.app.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Gender;
import com.example.app.domain.Member;
import com.example.app.mapper.GenderMapper;
import com.example.app.mapper.MemberMapper;

@Service
@Transactional(rollbackFor = Exception.class)
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberMapper mapper;

	@Autowired
	private GenderMapper gMapper;

	@Override
	public List<Member> findAll() throws Exception {
		return mapper.findAll();
	}

	@Override
	public boolean findByEmailAndPass(String email, String pass) throws Exception {
		Member member = mapper.findByEmail(email);
		if (member == null) {
			return false;
		}
		if (!BCrypt.checkpw(pass, member.getPass())) {
			return false;
		}
		return true;
	}

	@Override
	public void insert(Member member) throws Exception {
		String passH = BCrypt.hashpw(member.getPass(), BCrypt.gensalt());
		member.setPass(passH);
		mapper.insert(member);
	}

	@Override
	public void update(Member member) throws Exception {
		String passH = "";
		if (!member.getPass().isBlank()) {
			passH = BCrypt.hashpw(member.getPass(), BCrypt.gensalt());
		}
		member.setPass(passH);
		mapper.update(member);
	}

	@Override
	public void delete(Integer id) throws Exception {
		mapper.delete(id);
	}

	@Override
	public List<Gender> getAll() throws Exception {
		return gMapper.findAll();
	}

	@Override
	public Member selectEmail(String email) throws Exception {
		return mapper.findByEmail(email);
	}

	@Override
	public int totalPage(int numPage) throws Exception {
		double totalNum = mapper.count();
		return (int) Math.ceil(totalNum / numPage);
	}

	@Override
	public List<Member> limitMember(int page, int numPage) throws Exception {
		int offset = numPage * (page - 1);
		return mapper.limit(offset, numPage);
	}

}

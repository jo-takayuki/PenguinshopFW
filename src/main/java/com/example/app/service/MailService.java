package com.example.app.service;

import com.example.app.domain.Company;
import com.example.app.domain.Member;

public interface MailService {

	void memberEmail(Member member) throws Exception;

	void companyEmail(Company company) throws Exception;

}

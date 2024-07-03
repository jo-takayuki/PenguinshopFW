package com.example.app.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Company;
import com.example.app.domain.Member;

@Service
@Transactional(rollbackFor = Exception.class)
public class MailServiceImpl implements MailService {

	@Override
	public void memberEmail(Member member) throws Exception {
		String sex = "不明";
		if (member.getGenderId() != null) {
			if (member.getGenderId() == 1) {
				sex = "男性";
			} else if (member.getGenderId() == 2) {
				sex = "女性";
			} else {
				sex = "未選択";
			}
		}
		Integer age=0;
		if(member.getAge() !=null) {
			age = member.getAge();
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
		String ls = System.getProperty("line.separator");
		String msg = "ご登録ありがとうございます。" + ls + "登録情報は以下になります。" + ls + "ご登録日：" + sdf.format(date) + ls + "氏名："
				+ member.getName() + ls + "住所：" + member.getAddress1() + member.getAddress2() + ls + "年齢："
				+ age + "歳" + ls + "性別：" + sex + ls + "電話番号：" + member.getPhon() + ls + "メールアドレス："
				+ member.getEmail() + ls + "パスワード：" + member.getPass();
		SimpleEmail mail = new SimpleEmail();
		mail.setHostName("smtp.gmail.com");
		mail.setStartTLSEnabled(true);
		mail.setSslSmtpPort("465");
		mail.setAuthentication("taka94241@gmail.com", "beradbscdrawjhkk");
		mail.setFrom("taka94241@gmail.com", "登録完了", "ISO-2022-JP");
		mail.addTo(member.getEmail(), "システム管理者", "ISO-2022-JP");
		mail.setCharset("ISO-2022-JP");
		mail.setSubject("ご登録ありがとうございます。");
		mail.setMsg(msg);
		mail.send();
	}

	@Override
	public void companyEmail(Company company) throws Exception {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
		String ls = System.getProperty("line.separator");
		String msg = "ご登録ありがとうございます。" + ls + "登録情報は以下になります。" + ls + "ご登録日：" + sdf.format(date) + ls + "氏名："
				+ company.getName() + ls + "住所：" + company.getAddress1() + company.getAddress2() + ls + "電話番号："
				+ company.getPhon() + ls + "メールアドレス：" + company.getEmail() + ls + "パスワード：" + company.getPass();
		SimpleEmail mail = new SimpleEmail();
		mail.setHostName("smtp.gmail.com");
		mail.setStartTLSEnabled(true);
		mail.setSslSmtpPort("465");
		mail.setAuthentication("taka94241@gmail.com", "beradbscdrawjhkk");
		mail.setFrom("taka94241@gmail.com", "登録完了システム", "ISO-2022-JP");
		mail.addTo(company.getEmail(), "システム管理者", "ISO-2022-JP");
		mail.setCharset("ISO-2022-JP");
		mail.setSubject("ご登録ありがとうございます。");
		mail.setMsg(msg);
		mail.send();
	}

}

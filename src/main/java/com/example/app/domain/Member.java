package com.example.app.domain;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

	private Integer id;
	@NotBlank(groups = { InsertGroup.class })
	private String name;
	@NotBlank(groups = { InsertGroup.class })
	private String address1;
	private String address2;
	@Range(min = 0, max = 120)
	private Integer age;
	private Integer genderId;
	private String genderName;
	private String phon;
	@NotBlank(groups = { InsertGroup.class })
	@Email(groups = { InsertGroup.class })
	private String email;
	@NotBlank(groups = { InsertGroup.class })
	private String pass;
	@NotBlank(groups = { InsertGroup.class })
	private String passConf;
	private Integer typeId;
	private String typeName;
	private Date created;

}

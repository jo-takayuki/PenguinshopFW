package com.example.app.domain;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Admin {

	private Integer id;
	@NotBlank
	private String loginId;
	@NotBlank
	private String pass;

}

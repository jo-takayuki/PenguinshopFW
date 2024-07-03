package com.example.app.domain;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {

	@NotBlank
	private String email;
	@NotBlank
	private String pass;

}

package com.example.app.domain;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {

	private Integer id;
	@NotBlank
	private String name;
	private Integer itemId;
	private Date created;

}

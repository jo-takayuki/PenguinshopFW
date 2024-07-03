package com.example.app.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Cart {

	private Integer id;
	private String name;
	private Integer price;
	private String text;
	private Integer companyId;
	@NotNull
	@Min(1)
	private Integer unit;
	private Date created;
	private List<Picture> picture;
	private String PictureName;

}

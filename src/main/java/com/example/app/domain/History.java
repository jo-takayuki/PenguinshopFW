package com.example.app.domain;

import java.util.Date;

import lombok.Data;

@Data
public class History {

	private Integer id;
	private Integer memComId;
	private Integer typeId;
	private String typeName;
	private Integer itemId;
	private String itemName;
	private Integer unit;
	private Date created;

}

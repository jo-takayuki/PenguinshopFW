package com.example.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Receipt {
	
	private String name;
	private Integer price;
	private Integer unit;
	private Integer money;

}

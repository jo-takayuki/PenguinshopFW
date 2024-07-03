package com.example.app.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

	private Integer id;
	@NotBlank
	private String name;
	@NotNull
	private Integer price;
	private String text;
	private Integer companyId;
	private Date created;
	private List<Picture> picture;
	private List<MultipartFile> imageFiles;
	private String PictureName;

}

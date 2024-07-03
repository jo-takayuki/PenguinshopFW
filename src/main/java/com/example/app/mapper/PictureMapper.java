package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.Picture;

public interface PictureMapper {

	List<Picture> pictureAll(Integer Id) throws Exception;

	void insert(Picture picture) throws Exception;
	
	void delete(Integer id) throws Exception;

}

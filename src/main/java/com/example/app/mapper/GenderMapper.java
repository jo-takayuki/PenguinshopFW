package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.Gender;

public interface GenderMapper {

	List<Gender> findAll() throws Exception;

}
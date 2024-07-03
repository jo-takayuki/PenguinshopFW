package com.example.app.mapper;

import com.example.app.domain.Admin;

public interface AdminMapper {

	Admin select(String loginId) throws Exception;

}

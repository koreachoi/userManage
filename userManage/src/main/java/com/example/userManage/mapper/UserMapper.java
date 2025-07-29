package com.example.userManage.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.userManage.DTO.AccessDTO;
import com.example.userManage.DTO.UserDTO;

@Mapper
public interface UserMapper {
	
	UserDTO findByUserCode(String userCode);
	void insertAccess(AccessDTO accessDto);
}

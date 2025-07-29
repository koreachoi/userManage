package com.example.userManage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userManage.DTO.AccessDTO;
import com.example.userManage.DTO.UserDTO;
import com.example.userManage.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
		
	public UserDTO getUserByUserCode(String userCode) {
		return userMapper.findByUserCode(userCode);
	}
	
	public void insertAccess(AccessDTO accessDto) {};
}

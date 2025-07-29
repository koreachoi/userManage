package com.example.userManage.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userManage.DTO.AccessDTO;
import com.example.userManage.mapper.UserMapper;

@Service	
public class AccessService {

	@Autowired
	private UserMapper userMapper;
	
	public void createAccess(Long userId, String gamenId) {
		AccessDTO accessDto = new AccessDTO(userId, gamenId, LocalDateTime.now());
		userMapper.insertAccess(accessDto);
	}
}

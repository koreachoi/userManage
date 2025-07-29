package com.example.userManage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userManage.DTO.EmployeeDTO;
import com.example.userManage.mapper.EmployeeMapper;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	public List<EmployeeDTO> getByFilter(String firstNameKanji, 
										 String lastNameKanji, 
										   Long syouzokuKaisya, 
										   Long syokugyoKind, 
										   Long zaisyoku) {
		return employeeMapper.findByFilter(firstNameKanji, 
										   lastNameKanji, 
										   syouzokuKaisya, 
										   syokugyoKind, 
										   zaisyoku);
	}
	
	public void deleteBySyainId(Long syainId) {
		employeeMapper.deleteBySyainId(syainId);
	}
}

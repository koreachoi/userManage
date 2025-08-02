package com.example.userManage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userManage.DTO.EmployeeDTO;
import com.example.userManage.DTO.EmployeeRequestDTO;
import com.example.userManage.mapper.EmployeeMapper;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	public EmployeeDTO findBySyainId(Long syainId) {
		return employeeMapper.findBySyainId(syainId);
	}
	
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
	
	public List<EmployeeDTO> getAllEmployee(){
		return employeeMapper.findAllEmployee();
	}

	public void createSyain(EmployeeRequestDTO dto) {
	    employeeMapper.createSyain(dto);
	}
	
	public void updateSyain(EmployeeRequestDTO dto) {
		employeeMapper.updateSyain(dto);
	}
}

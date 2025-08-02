package com.example.userManage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.userManage.DTO.EmployeeDTO;
import com.example.userManage.DTO.EmployeeInsertDTO;

@Mapper
public interface EmployeeMapper {
	
	List<EmployeeDTO> findByFilter(@Param("firstNameKanji") String firstNameKanji,
								   @Param("lastNameKanji") String lastNameKanji,
								   @Param("syouzokuKaisya") Long syouzokuKaisya,
								   @Param("syokugyoKind") Long syokugyoKind,
								   @Param("zaisyoku") Long zaisyoku
								   );
	
	void deleteBySyainId(@Param("syainId") Long syainId);
	
	List<EmployeeDTO> findAllEmployee();
	
	void createSyain(EmployeeInsertDTO dto);
	
}

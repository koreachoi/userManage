package com.example.userManage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.userManage.DTO.SettingDTO;

@Mapper
public interface SettingMapper {
	
	SettingDTO findBySettingCategory(
			@Param("category1") Long category1,
			@Param("category2") Long category2, 
			@Param("category3") Long category3
			);
	
	List<SettingDTO> findBySettingCategoryList(
			@Param("category1") Long category1,
			@Param("category2") Long category2, 
			@Param("category3") Long category3
			);
}

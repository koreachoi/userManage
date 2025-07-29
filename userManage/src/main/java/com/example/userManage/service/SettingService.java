package com.example.userManage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.userManage.DTO.SettingDTO;
import com.example.userManage.mapper.SettingMapper;

@Service	
public class SettingService {
	
	@Autowired
	private SettingMapper settingMapper;
	
	public SettingDTO getSettingByCategory(Long category1, Long category2, Long category3) {
		return settingMapper.findBySettingCategory(category1, category2, category3);
	}
	
	public List<SettingDTO> getSettingByCategoryList(Long category1, Long category2, Long category3) {
	    return settingMapper.findBySettingCategoryList(category1, category2, category3);
	}

}

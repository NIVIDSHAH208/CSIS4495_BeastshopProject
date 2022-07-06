package com.beastshop.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.Setting;
import com.beastshop.common.entity.SettingCategory;

@Service
public class SettingService {
	@Autowired
	private SettingRepository repo;
	

	
	//this method returns general settings bag object	
	public List<Setting> getGeneralSetting() {
		
		return repo.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	

	
	
}
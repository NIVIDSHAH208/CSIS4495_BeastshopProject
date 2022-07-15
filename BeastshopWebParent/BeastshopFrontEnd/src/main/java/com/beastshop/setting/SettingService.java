package com.beastshop.setting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.setting.Setting;
import com.beastshop.common.entity.setting.SettingCategory;

@Service
public class SettingService {
	@Autowired
	private SettingRepository repo;
	

	
	//this method returns general settings bag object	
	public List<Setting> getGeneralSetting() {
		
		return repo.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}
	
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = repo.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(repo.findByCategory(SettingCategory.MAIL_TEMPLATES));
		
		return new EmailSettingBag(settings);
	}
	

	
	
}

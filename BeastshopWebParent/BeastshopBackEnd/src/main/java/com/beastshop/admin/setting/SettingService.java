package com.beastshop.admin.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beastshop.common.entity.setting.Setting;
import com.beastshop.common.entity.setting.SettingCategory;

@Service
public class SettingService {
	@Autowired
	private SettingRepository repo;
	
	//List all the settings
	public List<Setting> listAllSettings(){
		return (List<Setting>)repo.findAll();
	}
	
	//this method returns general settings bag object	
	public GeneralSettingBag getGeneralSetting() {
		List<Setting> settings = new ArrayList<>();
		
		List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = repo.findByCategory(SettingCategory.CURRENCY);
		
		settings.addAll(generalSettings);
		settings.addAll(currencySettings);
		
		return new GeneralSettingBag(settings);
	}
	
	
	//method to save settings object
	public void saveAll(Iterable<Setting> settings) {
		repo.saveAll(settings);
	}
	
	//method that returns list of mail server settings
	public List<Setting> getMailServerSettings() {
		return repo.findByCategory(SettingCategory.MAIL_SERVER);
	}
	
	//method that will return a list of mail template settings
	public List<Setting> getMailTemplateSettings(){
		return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}
	
	//method that will return a list of mail template settings
		public List<Setting> getCurrencySettings(){
			return repo.findByCategory(SettingCategory.CURRENCY);
		}
		
		public List<Setting> getPaymentSettings(){
			return repo.findByCategory(SettingCategory.PAYMENT);
		}
	
	
}

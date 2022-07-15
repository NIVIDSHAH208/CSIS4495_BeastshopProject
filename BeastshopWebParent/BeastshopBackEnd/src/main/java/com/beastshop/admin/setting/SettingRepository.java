package com.beastshop.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.beastshop.common.entity.setting.Setting;
import com.beastshop.common.entity.setting.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	
	
	public List<Setting> findByCategory(SettingCategory category);
}

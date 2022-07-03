package com.beastshop.admin.setting;

import org.springframework.data.repository.CrudRepository;

import com.beastshop.common.entity.Setting;

public interface SettingRepository extends CrudRepository<Setting, String> {
	
}

package com.beastshop.setting;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.beastshop.common.entity.Setting;
import com.beastshop.common.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	
	
	public List<Setting> findByCategory(SettingCategory category);
	
	@Query("SELECT s FROM Setting s WHERE s.category=?1 OR s.category=?2")
	public List<Setting> findByTwoCategory(SettingCategory cat1, SettingCategory cat2);
}

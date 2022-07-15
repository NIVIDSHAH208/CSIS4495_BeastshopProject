package com.beastshop.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.beastshop.common.entity.setting.Setting;
import com.beastshop.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {
	@Autowired SettingRepository repo;
	
	@Test
	public void testCreateGeneralSettings() {
		
//		Setting siteName = new Setting("SITE_NAME","Beastshop", SettingCategory.GENERAL);
		Setting siteLogo = new Setting("SITE_LOGO","Beastshop.png", SettingCategory.GENERAL);
		Setting copyright = new Setting("COPYRIGHT","Copyright (C) 2022 Beastshop Pvt. Ltd.", SettingCategory.GENERAL);
		
		repo.saveAll(List.of(siteLogo,copyright));
		Iterable<Setting> iterable = repo.findAll();
		
		assertThat(iterable).size().isGreaterThan(0);	
	}
	
	@Test
	public void testCreateSingleCurrencySettings() {
		
		Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION","before", SettingCategory.CURRENCY);
		repo.save(symbolPosition);
	}
	
	
	@Test
	public void testCreateCurrencySettings() {
		Setting currencyId = new Setting("CURRENCY_ID","1", SettingCategory.CURRENCY);
		Setting symbol = new Setting("CURRENCY_SYMBOL","$", SettingCategory.CURRENCY);
		Setting symbolPosition = new Setting("CURRENT_SYMBOL_POSITION","before", SettingCategory.CURRENCY);
		Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE","point", SettingCategory.CURRENCY);
		Setting decimalDigits = new Setting("DECIMAL_DIGITS","2", SettingCategory.CURRENCY);
		Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE","COMMA", SettingCategory.CURRENCY);
		
		repo.saveAll(List.of(currencyId,symbol,symbolPosition,decimalPointType,decimalDigits,thousandsPointType));
			
	}
	
	@Test
	public void testListSettingByCategory() {
		List<Setting> settings = repo.findByCategory(SettingCategory.GENERAL);
		settings.forEach(System.out::println);
		
	}
}

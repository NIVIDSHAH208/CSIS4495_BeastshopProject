package com.beastshop.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.beastshop.admin.FileUploadUtil;
import com.beastshop.common.entity.Currency;
import com.beastshop.common.entity.setting.Setting;

@Controller
public class SettingController {
	@Autowired
	private SettingService settingService;
	@Autowired
	private CurrencyRepository currencyRepo;

	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = settingService.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();
		model.addAttribute("pageTitle", "Settings-Beastshop Admin");
		model.addAttribute("listCurrencies", listCurrencies);

		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		return "settings/settings";
	}

	// method to handle the submission of the general form,
	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {

		GeneralSettingBag settingBag = settingService.getGeneralSetting();

		saveSiteLogo(multipartFile, settingBag);
		saveCurrencySymbol(request, settingBag);
		updateSettingsValuesFromForm(request, settingBag.list());

		redirectAttributes.addFlashAttribute("message", "General settings have been saved");

		return "redirect:/settings";
	}

	// Auto generated method
	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			settingBag.updateSiteLogo(value);
			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDirectory(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}

	// method to save currency symbol
	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
		// getting the value from the browser
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);

		if (findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}

	// For all the other values in the form
	private void updateSettingsValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}

		settingService.saveAll(listSettings);
	}

	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> mailServerSettings = settingService.getMailServerSettings();
		updateSettingsValuesFromForm(request, mailServerSettings);

		redirectAttributes.addFlashAttribute("message", "Mail server settings have been saved");
		return "redirect:/settings";
	}

	@PostMapping("/settings/save_mail_template")
	public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> mailTemplateSetting = settingService.getMailTemplateSettings();
		updateSettingsValuesFromForm(request, mailTemplateSetting);

		redirectAttributes.addFlashAttribute("message", "Mail template settings have been saved");
		return "redirect:/settings";
	}

}

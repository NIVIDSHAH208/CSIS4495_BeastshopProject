package com.beastshop.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.beastshop.common.entity.User;

public class UserCsvExporter extends AbstractExporter {
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv");

		// Importing csv bean writer from the super csv library
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		// Writing header to the file
		String[] csvHeader = { "UserId", "E-mail", "FirstName", "LastName", "Roles", "Enabled" };
		// Mapping the field to the array
		String[] userFieldMapping = { "id", "email", "firstname", "lastname", "roles", "enabled" };
		csvWriter.writeHeader(csvHeader);
		for (User user : listUsers) {
			csvWriter.write(user, userFieldMapping);
		}
		csvWriter.close();
	}
}

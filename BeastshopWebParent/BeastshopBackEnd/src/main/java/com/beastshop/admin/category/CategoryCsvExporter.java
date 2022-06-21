package com.beastshop.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.beastshop.admin.AbstractExporter;
import com.beastshop.common.entity.Category;
import com.beastshop.common.entity.User;

public class CategoryCsvExporter extends AbstractExporter {
	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv","beastUsers_");

		// Importing csv bean writer from the super csv library
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		// Writing header to the file
		String[] csvHeader = { "Category ID", "Category name" };
		// Mapping the field to the array
		String[] categoryFieldMapping = { "id", "name" };
		csvWriter.writeHeader(csvHeader);
		for (Category category : listCategories) {
			category.setName(category.getName().replace("--"," "));
			csvWriter.write(category, categoryFieldMapping);
		}
		csvWriter.close();
	}
}

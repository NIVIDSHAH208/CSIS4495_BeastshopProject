package com.beastshop.admin.brand;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.beastshop.admin.AbstractExporter;
import com.beastshop.common.entity.Brand;
import com.beastshop.common.entity.Category;

public class BrandCsvExporter extends AbstractExporter{
	public void export(List<Brand> listBrands, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "beastUsers_");

		// Importing csv bean writer from the super csv library
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		// Writing header to the file
		String[] csvHeader = { "BrandID", "Brand name", "Categories" };
		// Mapping the field to the array
		String[] categoryFieldMapping = { "id", "name","categories" };
		csvWriter.writeHeader(csvHeader);
		for (Brand brand : listBrands) {
			
			csvWriter.write(brand, categoryFieldMapping);
		}
		csvWriter.close();
	}
}

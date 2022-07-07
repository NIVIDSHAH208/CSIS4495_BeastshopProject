package com.beastshop.admin.customer;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.beastshop.admin.AbstractExporter;

import com.beastshop.common.entity.Customer;

public class CustomerCsvExporter extends AbstractExporter {
	public void export(List<Customer> listCustomers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "beastCustomers_");

		// Importing csv bean writer from the super csv library
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		// Writing header to the file
		String[] csvHeader = { "CustomerID", "Firstname","Lastname", "Email", "City","State","Country","Enabled" };
		// Mapping the field to the array
		String[] customersFieldMapping = { "id", "firstName","lastName","email","city","state","country","enabled" };
		csvWriter.writeHeader(csvHeader);
		for (Customer customer : listCustomers) {
			
			csvWriter.write(customer, customersFieldMapping);
		}
		csvWriter.close();
}
	}

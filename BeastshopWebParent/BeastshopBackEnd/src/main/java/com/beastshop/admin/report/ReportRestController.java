package com.beastshop.admin.report;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportRestController {
	@Autowired private MasterOrderReportService masterOrderReportService;
	@Autowired private OrderDetailReportService orderDetailReportService;
	
	@GetMapping("/reports/sales_by_date/{period}")
	public List<ReportItem> getReportDataByDatePeriod(@PathVariable("period") String period){
		System.out.println("Report period: "+period);
		
		switch (period) {
		case "last_7_days": 
			return masterOrderReportService.getReportDataLast7Days(ReportType.DAY);
		case "last_28_days": 
			return masterOrderReportService.getReportDataLast28Days(ReportType.DAY);
		case "last_6_months": 
			return masterOrderReportService.getReportDataLast6Months(ReportType.MONTH);
		case "last_year": 
			return masterOrderReportService.getReportDataLastYear(ReportType.MONTH);
		default:
			return masterOrderReportService.getReportDataLast7Days(ReportType.DAY);
		}	
	}
	
	@GetMapping("/reports/{groupBy}/{period}")
	public List<ReportItem> getReportDataByCategoryOrProduct(@PathVariable String groupBy, @PathVariable String period){
		ReportType reportType = ReportType.valueOf(groupBy.toUpperCase());
		
		switch (period) {
		case "last_7_days": 
			return orderDetailReportService.getReportDataLast7Days(reportType);
		case "last_28_days": 
			return orderDetailReportService.getReportDataLast28Days(reportType);
		case "last_6_months": 
			return orderDetailReportService.getReportDataLast6Months(reportType);
		case "last_year": 
			return orderDetailReportService.getReportDataLastYear(reportType);
		default:
			return orderDetailReportService.getReportDataLast7Days(reportType);
		}	
	}
	
	
}	

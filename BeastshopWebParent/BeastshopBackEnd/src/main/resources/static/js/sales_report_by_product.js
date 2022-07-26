//Sales report by date

var data, chartOptions;

$(document).ready(function(){
	setupButtonEventHandlers("_product",loadSalesReportByDateForProduct);
});

function loadSalesReportByDateForProduct(period){
	requestURL = contextPath + "reports/product/"+period;
	
	$.get(requestURL, function(responseJSON){
		prepareChartDataForSalesReportByProduct(responseJSON)
		customizeChartForSalesReportByProduct()
		formatChartData(data, 2, 3)
		drawChartForSalesReportByProduct(period);
		setSalesAmount(period, "_product", "Total Products")
	});
}

function prepareChartDataForSalesReportByProduct(responseJSON){
	data = new google.visualization.DataTable();
	data.addColumn('string','Product');
	data.addColumn('number','Quantity');
	data.addColumn('number','Gross sales');
	data.addColumn('number','Net sales');
	
	totalGrossSales=0.0
	totalNetSales=0.0
	totalItems=0.0
	
	//console.log(responseJSON)
	$.each(responseJSON, function(index, reportItem){
	
		data.addRows([[reportItem.identifier, reportItem.productsCount, reportItem.grossSales, reportItem.netSales]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseInt(reportItem.productsCount);
	});
}

function customizeChartForSalesReportByProduct(){
	chartOptions = {
		height:360,
		width:'95%',
		showRowNumber: true,
		page: 'enable',
		sortColumn: 2,
		sortAscending: false
		
	};	
}

function drawChartForSalesReportByProduct(){
	var salesChart = new google.visualization.Table(document.getElementById("chart_sales_by_product"));
	salesChart.draw(data, chartOptions);
	
}


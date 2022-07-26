//Sales report by date

var data, chartOptions, totalGrossSales, totalNetSales, totalItems;

$(document).ready(function(){
	setupButtonEventHandlers("_date",loadSalesReportByDate);
});

function loadSalesReportByDate(period){
	requestURL = contextPath + "reports/sales_by_date/"+period;
	
	$.get(requestURL, function(responseJSON){
		prepareChartDataForSalesReportByDate(responseJSON)
		customizeChartForSalesReportByDate(period)
		formatChartData(data, 1, 2)
		drawChartForSalesReportByDate(period);
		setSalesAmount(period, "_date", "Total Items")
	});
}

function prepareChartDataForSalesReportByDate(responseJSON){
	data = new google.visualization.DataTable();
	data.addColumn('string','Date');
	data.addColumn('number','Gross sales');
	data.addColumn('number','Net sales');
	data.addColumn('number','Orders');
	totalGrossSales=0.0
	totalNetSales=0.0
	totalItems=0.0
	//console.log(responseJSON)
	$.each(responseJSON, function(index, reportItem){
	
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, reportItem.ordersCount]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseInt(reportItem.ordersCount);
	});
}

function customizeChartForSalesReportByDate(period){
	chartOptions = {
		title: getChartTitle(period),
		'height':360,
		'width':$(window).width()*0.95,
		legend:{position:'top'},
		series:{
			0:{targetAxisIndex:0},
			1:{targetAxisIndex:0},
			2:{targetAxisIndex:1}
		},
		vAxes:{
			0: {title:'Sales amount', format:'currency'},
			1: {title:'Number of Orders'}
		}
	};
	
	
}

function drawChartForSalesReportByDate(){
	var salesChart = new google.visualization.ColumnChart(document.getElementById("chart_sales_by_date"));
	salesChart.draw(data, chartOptions);
	
}


//Sales report by date

var data, chartOptions, totalGrossSales, totalNetSales, totalOrders;

$(document).ready(function(){
	$(".button-sales-by-date").on("click", function(){
	
		$(".button-sales-by-date").each(function(e){
			$(this).removeClass('btn-primary').addClass("btn-light");
		});
		$(this).removeClass('btn-light').addClass("btn-primary")
	
		period = $(this).attr("period")
		loadSalesReportByDate(period);
	});
});

function loadSalesReportByDate(period){
	requestURL = contextPath + "reports/sales_by_date/"+period;
	
	$.get(requestURL, function(responseJSON){
		prepareChartData(responseJSON)
		customizeChart(period)
		drawChart(period);
	});
}

function prepareChartData(responseJSON){
	data = new google.visualization.DataTable();
	data.addColumn('string','Date');
	data.addColumn('number','Gross sales');
	data.addColumn('number','Net sales');
	data.addColumn('number','Orders');
	totalGrossSales=0.0
	totalNetSales=0.0
	totalOrders=0.0
	//console.log(responseJSON)
	$.each(responseJSON, function(index, reportItem){
	
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, reportItem.ordersCount]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalOrders += parseInt(reportItem.ordersCount);
	});
}

function customizeChart(period){
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
	
	var formatter = new google.visualization.NumberFormat({
		prefix: prefixCurrencySymbol,
		suffix: suffixCurrencySymbol,
		decimalSymbol:decimalPointType,
		groupingSymbol: thousandsPointType,
		fractionDigits: decimalDigits
	});
	
	formatter.format(data,1);
	formatter.format(data,2);
}

function drawChart(period){
	var salesChart = new google.visualization.ColumnChart(document.getElementById("chart_sales_by_date"));
	salesChart.draw(data, chartOptions);
	$("#textTotalGrossSales").text(formatCurrency(totalGrossSales));
	$("#textTotalNetSales").text(formatCurrency(totalNetSales));
	days = getDenominator(period);
	$("#textAvgGrossSales").text(formatCurrency(totalGrossSales/days));
	$("#textAvgNetSales").text(formatCurrency(totalNetSales/days));
	$("#textTotalOrders").text(totalOrders);
	
}

function formatCurrency(amount){
	formattedAmount = $.number(amount, decimalDigits, decimalPointType, thousandsPointType);
	return prefixCurrencySymbol+formattedAmount+suffixCurrencySymbol;
}

function getDenominator(period){
	if(period=="last_7_days") return 7;
	if(period=="last_28_days") return 28;
	if(period=="last_6_months") return 6;
	if(period=="last_year") return 12;
	return 7;
}

function getChartTitle(period){
	if(period=="last_7_days") return "Sales in last 7 days";
	if(period=="last_28_days") return "Sales in last 28 days";
	if(period=="last_6_months") return "Sales in last 6 months";
	if(period=="last_year") return "Sales in last year";
	return "";
}
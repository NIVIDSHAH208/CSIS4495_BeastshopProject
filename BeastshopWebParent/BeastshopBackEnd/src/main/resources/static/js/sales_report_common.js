function setupButtonEventHandlers(reportType, callbackFunction){
	$(".button-sales-by"+reportType).on("click", function(){
	
		$(".button-sales-by"+reportType).each(function(e){
			$(this).removeClass('btn-primary').addClass("btn-light");
		});
		$(this).removeClass('btn-light').addClass("btn-primary")
	
		period = $(this).attr("period")
		callbackFunction(period);
	});
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

function setSalesAmount(period, reportType, labelTotalItems){
	$("#textTotalGrossSales"+reportType).text(formatCurrency(totalGrossSales));
	$("#textTotalNetSales"+reportType).text(formatCurrency(totalNetSales));
	days = getDenominator(period);
	$("#textAvgGrossSales"+reportType).text(formatCurrency(totalGrossSales/days));
	$("#textAvgNetSales"+reportType).text(formatCurrency(totalNetSales/days));
	$("#labelTotalItems"+reportType).text(labelTotalItems);
	$("#textTotalItems"+reportType).text(totalItems);
}

function formatChartData(data, columnIndex1, columnIndex2){
	var formatter = new google.visualization.NumberFormat({
		prefix: prefixCurrencySymbol,
		suffix: suffixCurrencySymbol,
		decimalSymbol:decimalPointType,
		groupingSymbol: thousandsPointType,
		fractionDigits: decimalDigits
	});
	
	formatter.format(data,columnIndex1);
	formatter.format(data,columnIndex2);
}
/**
 *Author: Nivid
 */
 
 var fieldProductCost;
 var fieldSubtotal;
 var fieldShippingCost;
 var fieldTax;
 var fieldTotal;
 
 $(document).ready(function(){
 	//alert("hello world");
 	
 	fieldProductCost=$("#productCost");
 	fieldSubtotal=$("#subtotal");
 	fieldShippingCost=$("#shippingCost");
 	fieldTax=$("#tax");
 	fieldTotal=$("#total");
 	
 	formatOrderAmounts();
 	formatProductAmounts();
 	
 	$("#productList").on("change",".quantity-input", function(e){
 		updateSubtotalWhenQuantityChange($(this));
 		updateOrderAmounts();
 	});
 	
 	$("#productList").on("change",".price-input", function(e){
 		updateSubtotalWhenPriceChange($(this));
 		updateOrderAmounts();
 	});
 	
 	$("#productList").on("change",".cost-input", function(e){
 		updateOrderAmounts();
 	});
 	
 	$("#productList").on("change",".ship-input", function(e){
 		updateOrderAmounts();
 	});
 	
 });
 
 function processFormBeforeSubmit(){
 	setCountryName();
 	removeThousandSeparator(fieldProductCost);
 	removeThousandSeparator(fieldSubtotal);
 	removeThousandSeparator(fieldShippingCost);
 	removeThousandSeparator(fieldTax);
 	removeThousandSeparator(fieldTotal);
 	
 	$(".cost-input").each(function(e){
 		removeThousandSeparator($(this));
 	});
 	$(".ship-input").each(function(e){
 		removeThousandSeparator($(this));
 	});
 	$(".price-input").each(function(e){
 		removeThousandSeparator($(this));
 	});
 	$(".subtotal-output").each(function(e){
 		removeThousandSeparator($(this));
 	});
 }
 
 function setCountryName(){
 	selectedCountry = $("#country option:selected");
 	countryName = selectedCountry.text();
 	$("#countryName").val(countryName);
 }
 
 function removeThousandSeparator(fieldRef){
 	fieldRef.val(fieldRef.val().replace(",",""));
 }
 
 function updateOrderAmounts(){
 	//alert("apples");
 	totalCost=0.0;
 	
 	$(".cost-input").each(function(e){
 		costInputField=$(this);
 		rowNumber=costInputField.attr("rowNumber");
 		quantityValue=$("#quantity"+rowNumber).val();
 		
 		productCost = getNumberValueRemovedThousandSeparator(costInputField);
 		totalCost += productCost * parseInt(quantityValue);
 	});
 	
 	setAndFormatNumberForField("productCost", totalCost);
 	
 	orderSubtotal=0.0;
 	$(".subtotal-output").each(function(e){
 		productSubtotal = getNumberValueRemovedThousandSeparator($(this));
 		orderSubtotal += productSubtotal;
 	});
 	setAndFormatNumberForField("subtotal", orderSubtotal);
 	
 	shippingCost = 0.0;
 	$(".ship-input").each(function(e){
 		productShip = getNumberValueRemovedThousandSeparator($(this));
 		shippingCost += productShip;
 	});
 	setAndFormatNumberForField("shippingCost",shippingCost);
 	
 	tax = getNumberValueRemovedThousandSeparator(fieldTax);
 	orderTotal = orderSubtotal + tax + shippingCost;
 	setAndFormatNumberForField("total",orderTotal);
 }
 
 function setAndFormatNumberForField(fieldId, fieldValue){
 	formattedValue = $.number(fieldValue,2);
 	$("#"+fieldId).val(formattedValue);
 }
 
 function getNumberValueRemovedThousandSeparator(fieldRef){
 	fieldValue=fieldRef.val().replace(",","");
 	return parseFloat(fieldValue);
 }
 
 function updateSubtotalWhenPriceChange(input){
 	priceValue=getNumberValueRemovedThousandSeparator(input);
 	
 	rowNumber=input.attr("rowNumber");
 	
 	quantityField=$("#quantity"+rowNumber);
 	
 	quantityValue = quantityField.val();
 	
 	newSubTotal=parseFloat(quantityValue)*priceValue;
 	
 	
 	setAndFormatNumberForField("subtotal"+rowNumber, newSubTotal);
 }
 
 function updateSubtotalWhenQuantityChange(input){
 	quantityValue=input.val();
 	rowNumber=input.attr("rowNumber")
 	priceField=$("#price"+rowNumber);
 	priceValue=getNumberValueRemovedThousandSeparator(priceField);
 	
 	newSubTotal=parseFloat(quantityValue)*priceValue;
 	
 	setAndFormatNumberForField("subtotal"+rowNumber, newSubTotal);
 }
 
 function formatProductAmounts(){
 	$(".cost-input").each(function(){
 		formatNumberForField($(this))
 	});
 	$(".price-input").each(function(){
 		formatNumberForField($(this))
 	});
 	$(".subtotal-output").each(function(){
 		formatNumberForField($(this))
 	});
 	$(".ship-input").each(function(){
 		formatNumberForField($(this))
 	});
 }
 
 function formatOrderAmounts(){
 	formatNumberForField(fieldProductCost);
 	formatNumberForField(fieldSubtotal);
 	formatNumberForField(fieldShippingCost);
 	formatNumberForField(fieldTax);
 	formatNumberForField(fieldTotal);
 }
 
 function formatNumberForField(fieldRef){
 	fieldRef.val($.number(fieldRef.val(),2));
 }
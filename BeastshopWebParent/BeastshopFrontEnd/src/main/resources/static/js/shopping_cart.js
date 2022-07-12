/**
 * Nivid
 */
 
 decimalDigits;
 decimalSeparator = decimalPointType == "COMMA"?',':'.';
 thousandsSeparator = thousandsPointType == "COMMA"?',':'.';
 
 $(document).ready(function(){
 	$(".linkMinus").on("click", function(evt){
 		evt.preventDefault();
 		decreaseQuantity($(this));
 	});
 	
 	$(".linkPlus").on("click", function(evt){
 		evt.preventDefault();
 		increaseQuantity($(this));
 	});
 	
 	$(".linkRemove").on("click", function(evt){
 		evt.preventDefault();
 		removeProduct($(this))
 	});
 });
 
 function removeProductHTML(rowNumber){
 	$("#row"+rowNumber).remove()
 	$("#blankLine"+rowNumber).remove()
 }
 
 function removeProduct(link){
 	url = link.attr("href");
 	$.ajax({
		type:"DELETE",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response){
		rowNumber=link.attr("rowNumber")
		removeProductHTML(rowNumber);
		updateTotal();
		updateCountNumbers();
		showModalDialog("Product removed", response);
	}).fail(function(){
		showErrorModal("Server encountered an error while deleting product from the cart");
	});
 }
 
 
 function updateCountNumbers(){
 	$(".divCount").each(function(index, element){
 		element.innerHTML=""+(index+1);
 	});
 }
 
 
 function updateQuantity(productId, quantity){
	//making an ajax call
	url = contextPath+"cart/update/"+productId+"/"+quantity;
	
	$.ajax({
		type:"POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(updatesSubtotal){
		updateSubtotal(updatesSubtotal, productId);
		updateTotal();
	}).fail(function(){
		showErrorModal("Server encountered an error while updating product to cart");
	});
 }
 
 function increaseQuantity(link){
 		productId = link.attr("pid");
 		quantityInput = $("#quantity"+productId)
 		newQuantity = parseInt(quantityInput.val())+1;
 		if(newQuantity<=5){
 			quantityInput.val(newQuantity);
 			updateQuantity(productId, newQuantity);
 		}else{
 			showWarningModal('Maximum quantity is five');
 		}
 }
 
 function decreaseQuantity(link){
 		productId = link.attr("pid");
 		quantityInput = $("#quantity"+productId)
 		newQuantity = parseInt(quantityInput.val())-1;
 		if(newQuantity>0){
 			quantityInput.val(newQuantity)
 			updateQuantity(productId, newQuantity);
 		}else{
 			showWarningModal('Minimum quantity is one');
 		}
 }
 
 function updateSubtotal(updatesSubtotal, productId){
 	$("#subtotal"+productId).text(formatCurrency(updatesSubtotal))
 }
 
 function updateTotal(){
 	totalPrice=0.0;
 	productCount=0;
 	
 	$(".subtotal").each(function(index, element){
 		productCount++;
 		numFinalTotal = clearCurrencyFormat(element.innerHTML);
 		totalPrice+=parseFloat(numFinalTotal);
 	});
 	
 	if (productCount < 1){
 		showEmptyShoppingCart();
 	}else{
 		$("#finalTotal").text(formatCurrency(totalPrice));
 	}
 }
 
 function showEmptyShoppingCart(){
	$("#sectionTotal").hide()
	$("#sectionEmptyCartMessage").removeClass("d-none");
 }
 
 function formatCurrency(amount){
 	return $.number(amount, decimalDigits, decimalSeparator, thousandsSeparator)
 }
 
 function clearCurrencyFormat(numberString){
 	result = numberString.replaceAll(thousandsSeparator,"");
 	return result.replaceAll(decimalSeparator,".");
 }
 
 
/**
 * Nivid
 */
 
 $(document).ready(function(){
 	$(".linkMinus").on("click", function(evt){
 		evt.preventDefault();
 		decreaseQuantity($(this));
 	});
 	
 	$(".linkPlus").on("click", function(evt){
 		evt.preventDefault();
 		increaseQuantity($(this));
 	});
 });
 
 
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
 	formattedSubtotal = $.number(updatesSubtotal,2);
 	$("#subtotal"+productId).text(formattedSubtotal)
 }
 
 function updateTotal(){
 	totalPrice=0.0;
 	$(".subtotal").each(function(index, element){
 		numFinalTotal = element.innerHTML.replaceAll(",","");
 		totalPrice+=parseFloat(numFinalTotal);
 	});
 	formattedTotal = $.number(totalPrice,2)
 	$("#finalTotal").text(formattedTotal);
 }
 
 
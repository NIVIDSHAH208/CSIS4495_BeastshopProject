$(document).ready(function(){
	$("#buttonAdd2Cart").on("click", function(evt){
		addToCart();
	});
});

function addToCart(){
	//getting the quantity
	quantity = $("#quantity"+productId).val();
	
	//making an ajax call
	url = contextPath+"cart/add/"+productId+"/"+quantity;
	
	$.ajax({
		type:"POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response){
		showModalDialog("Added to cart", response);
	}).fail(function(){
		showErrorModal("Server encountered an error while adding product to cart");
	});
	
}
/**
 * Author: Nivid
 */
 
 var productDetailCount;
 
 $(document).ready(function(){
 	productDetailCount = $(".hiddenProductId").length;
 
 	$("#products").on("click","#linkAddProduct", function(e){
	 	//alert("Apples")
 		e.preventDefault();
 		link =$(this)
 		url=link.attr("href");
 		
 		$("#addProductModal").on("shown.bs.modal", function(){
 			$(this).find("iframe").attr("src",url);
 		});
 		
 		$("#addProductModal").modal();
 	});
 });
 
 function addProduct(productId, productName){
 	$("#addProductModal").modal("hide");
 	getShippingCost(productId);
 }
 
 function getShippingCost(productId){
 	 selectedCountry = $("#country option:selected")
 	 countryId = selectedCountry.val();
 	 state = $("#state").val();
 	 if(state.length==0){
 	 	state=$("#city").val()
 	 }
 	 
 	 requestURL = contextPath +"get_shipping_cost";
 	 params = {productId: productId, countryId:countryId, state:state};
 	 
 	 $.ajax({
		type: "POST",
		url: requestURL,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data : params
	}).done(function(shippingCost){
		getProductInfo(productId, shippingCost);
	}).fail(function(err){
		showWarningModal(err.responseJSON.message);
		shippingCost=0.0;
		getProductInfo(productId, shippingCost);
	}).always(function(){
		$("#addProductModal").modal("hide");
	});
 
 }
 
 function getProductInfo(productId, shippingCost){
 	requestUrl = contextPath + "products/get/"+productId;
 	$.get(requestUrl, function(productJSON){
 		//console.log(productJSON)
 		productName = productJSON.name;
 		mainImagePath = contextPath.substring(0, contextPath.length-1) + productJSON.imagePath;
 		productCost = $.number(productJSON.cost, 2);
 		productPrice = $.number(productJSON.price, 2);
 		htmlCode = generateProductCode(productId, productName, mainImagePath, productCost, productPrice, shippingCost);
 		$("#productList").append(htmlCode);
 		updateOrderAmounts();
 	}).fail(function(err){
 		showWarningModal(err.responseJSON.message)
 	});
 }
 
 function isProductAlreadyAdded(productId){
 	productExists=false;
 	$(".hiddenProductId").each(function(e){
 		aproductId = $(this).val();
 		if(aproductId == productId){
 			productExists=true;
 			return;
 		}
 	});	
 	return productExists;
 }
 

 function generateProductCode(productId, productName, mainImagePath, productCost, productPrice, shippingCost){
 	nextCount = productDetailCount + 1;
 	productDetailCount++;
 	quantityId = "quantity"+nextCount;
 	priceId = "price"+nextCount;
 	subtotalId = "subtotal"+nextCount;
 	rowId="row"+nextCount;
 	blankLineId = "blankLine"+nextCount
 	
 	htmlCode= `
 		<div class="border rounded p-1" id="${rowId}">
 				<input type="hidden" name="detailId" value="0" />
				<input type="hidden" name="productId" value="${productId}" class="hiddenProductId" />
					<div class="row">
						<div class="col-1">
							<div class="divCount">${nextCount}</div>
							<div><a class="fas fa-trash-can icon-dark linkRemove" href="" rowNumber="${nextCount}"></a></div>
						</div>
						<div class="col-3">
							<img src="${mainImagePath}" class="img-fluid" />
						</div>
					</div>

					<div class="row m-2">
						<b>${productName}</b>
					</div>

					<div class="row m-2">
						<table>
							<tr>
								<td>Product Cost:</td>
								<td><input type="text" required
									name="productDetailCost"
									class="form-control m-1 cost-input" style="max-width: 140px"
									rowNumber="${nextCount}"
									value="${productCost}" /></td>
							</tr>
							<tr>
								<td>Product quantity:</td>
								<td><input type="number" step="1" min="1" max="5"
									rowNumber="${nextCount}" required
									name="quantity"
									class="form-control m-1 quantity-input"
									style="max-width: 140px" id="${quantityId}"
									value="1" /></td>
							</tr>
							<tr>
								<td>Unit price:</td>
								<td><input type="text" required
									name="productPrice"
									class="form-control m-1 price-input" style="max-width: 140px"
									rowNumber="${nextCount}"
									id="${priceId}"
									value="${productPrice}" /></td>
							</tr>
							<tr>
								<td>Subtotal:</td>
								<td><input type="text" readonly
								name="productSubtotal"
									class="form-control m-1 subtotal-output"
									id="${subtotalId}" style="max-width: 140px"
									value="${productPrice}" /></td>
							</tr>
							<tr>
								<td>Shipping Cost:</td>
								<td><input type="text" required
								name="productShipCost"
									class="form-control m-1 ship-input" style="max-width: 140px"
									rowNumber="${nextCount}"
									value="${shippingCost}" /></td>
							</tr>
						</table>
					</div>
				</div>
				<div id="${blankLineId}" class="row">&nbsp;</div>
 	`;
 	
 	return htmlCode;
 }
var returnModal;
var modalTitle;
var fieldNote;
var orderId;
var divReason, divMessage;
var firstButton, secondButton;

$(document).ready(function(){
	returnModal = $("#returnOrderModal")
	modalTitle = $("#returnOrderModalTitle")
	fieldNote = $("#returnNote");
	divReason = $("#divReason");
	divMessage = $("#divMessage");
	firstButton = $("#firstButton");
	secondButton = $("#secondButton");
	
	handleReturnOrderLink()
	
});

function showReturnModalDialog(link){
	divMessage.hide();
	divReason.show();
	secondButton.text("Cancel");
	firstButton.show();
	fieldNote.val("");
	
	orderId = link.attr("orderId")
	modalTitle.text("Return Order Id #"+orderId)
	returnModal.modal('show')
}

function showMessageModal(message){
	divReason.hide();
	secondButton.text("Close");
	firstButton.hide();
	divMessage.text(message);
	divMessage.show();
}

function handleReturnOrderLink(){
	$(".linkReturnOrder").on("click", function(e){
		e.preventDefault();
		showReturnModalDialog($(this));
	});
}

function submitReturnOrderForm(){
	reason = $("input[name='returnReason']:checked").val();
	note = fieldNote.val();
	
	sendReturnOrderRequest(reason, note);
	
	return false;
}

function sendReturnOrderRequest(reason, note){
	requestURL = contextPath + "orders/return";
	requestBody = {orderId: orderId, reason: reason, note: note};
	
	$.ajax({
		type:"POST",
		url: requestURL,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(requestBody),
		contentType: 'application/json'
	}).done(function(returnResponse){
		showMessageModal("Return request has been sent");
		updateStatusTextAndHideReturnButton(orderId);
	}).fail(function(err){
		showMessageModal(err.responseText)
	});
}

function updateStatusTextAndHideReturnButton(orderId){
	$(".textOrderStatus"+orderId).each(function(){
		$(this).text("RETURN_REQUESTED")
	});
	
	$(".linkReturn"+orderId).each(function(){
		$(this).hide();
	});
	
}


/**
 * 
 */
var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var labelCountryCode;
var fieldCountryCode


$(document).ready(function(){
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropdownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry=$("#buttonUpdateCountry");
	buttonDeleteCountry=$("#buttonDeleteCountry");
	labelCountryName=$("#labelCountryName");
	fieldCountryName=$("#fieldCountryName");
	fieldCountryCode=$("#fieldCountryCode");
	
	buttonLoad.click(function(){
		loadCountries();
	});
	
	dropDownCountry.on("change", function(){
		changeFormStateToSelectedCountry();
	});
	
	buttonAddCountry.click(function(){
		if(buttonAddCountry.val()=="Add"){
			addCountry();
		}else{		
			changeFormStateToNewCountry();
		}
	});
	
	buttonUpdateCountry.click(function(){
		updateCountry();
	});
	
	buttonDeleteCountry.click(function(){
		deleteCountry();
	});
	
});

function deleteCountry(){
	countryId=dropDownCountry.val().split("-")[0];
	url = contextPath+ "countries/delete/"+countryId;
	
	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(countryId){
		$("#dropdownCountries option[value='"+optionValue+"']").remove();
		changeFormStateToNewCountry()
		showToastMessage("The country has been deleted");
	}).fail(function(){
		showToastMessage("Error-Could not connect to the server");
	});
}

function updateCountry(){
	url = contextPath+ "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	countryId=dropDownCountry.val().split("-")[0];
	
	jsonData= {id:countryId, name: countryName, code:countryCode};
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		$("#dropdownCountries option:selected").val(countryId+"-"+countryCode);
		$("#dropdownCountries option:selected").text(countryName);
		showToastMessage("The new country has been updated");
		
		changeFormStateToNewCountry();
	}).fail(function(){
		showToastMessage("Error-Could not connect to the server");
	});
}

function addCountry(){
	url = contextPath+ "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	
	jsonData= {name: countryName, code:countryCode};
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		showToastMessage("The new country has been added");
	}).fail(function(){
		showToastMessage("Error-Could not connect to the server");
	});
}


function selectNewlyAddedCountry(countryId, countryCode, countryName){
	optionValue=countryId+"-"+countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
	
	$("#dropdownCountries option[value='"+optionValue+"']").prop("selected", true);
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
	
}

function changeFormStateToNewCountry(){
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name:");
	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);
	
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

function changeFormStateToSelectedCountry(){
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);
	
	labelCountryName.text("Selected Country:")
	selectedCountryName = $("#dropdownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName)
	
	countryCode=dropDownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode)
	

}

function loadCountries(){
	url = contextPath+"countries/list";
	$.get(url, function(responseJSON){
		dropDownCountry.empty();
		$.each(responseJSON, function(index, country){
			optionValue=country.id+"-"+country.code;
			//alert(optionValue);
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
	}).done(function(){
		buttonLoad.val("Refresh Country List");
		//alert("All countries have been loaded");
		showToastMessage("All countries have been loaded");
	}).fail(function(){
		showToastMessage("Error-Could not connect to the server");
	})
}

function showToastMessage(message){
	$("#toastMessage").text(message)
	$(".toast").toast("show")
}
 
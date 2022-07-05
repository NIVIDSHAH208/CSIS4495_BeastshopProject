var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var dropDownStates;
var labelStateName;
var fieldStateName;
var buttonLoad4States;
var dropDownCountry4States;

$(document).ready(function(){
	buttonLoad4States=$("#buttonLoadCountriesForStates");
	buttonAddState=$("#buttonAddState");
	buttonUpdateState=$("#buttonUpdateState");
	buttonDeleteState=$("#buttonDeleteState");
	dropDownStates=$("#dropDownStates");
	labelStateName=$("#labelStateName");
	fieldStateName=$("#fieldStateName");
	dropDownCountry4States=$("#dropDownCountriesForStates");
	
	buttonLoad4States.click(function(){
		loadCountries4States();
	});
	
	dropDownCountry4States.on("change", function(){
		loadStates4Countries();
	});
	
	dropDownStates.on("change", function(){
		changeFormStateToSelectedState();
	});
	
	buttonAddState.click(function(){
		if(buttonAddState.val()=="Add"){
			addState();
		}else{
			changeFormStateToNew();
		}
	});
	
	buttonUpdateState.click(function(){
		updateState();
	});
	
	buttonDeleteState.click(function(){
		deleteState();
	});
	
});

function deleteState(){
	stateId = dropDownStates.val();
	url=contextPath+"states/delete/"+stateId;
	
	$.get(url, function(){
		$("#dropDownStates option[value='"+stateId+"']").remove();
		changeFormStateToNew();
	}).done(function(){
		showToastMessage("The state has been deleted");
	}).fail(function(){
		showToastmessage("Error-cound not connect to the server");
	});
}

function updateState(){
	url=contextPath+"states/save";
	stateId = dropDownStates.val();
	stateName=fieldStateName.val();
	
	selectedCountry=$("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	
	jsonData = {id:stateId ,name:stateName, country:{id:countryId, name:countryName}};
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId){
		$("#dropDownStates option:selected").text(stateName);
		showToastMessage("The new state has been updated");
		changeFormStateToNew();
	}).fail(function(){
		showToastMessage("Error-Could not connect to the server");
	});
}

function addState(){
	url=contextPath+"states/save";
	stateName=fieldStateName.val();
	selectedCountry=$("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val()
	countryName = selectedCountry.text()
	jsonData = {name:stateName, country:{id:countryId, name:countryName}};
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId){
		selectNewlyAddedState(stateId, stateName);
		showToastMessage("The new state has been added");
	}).fail(function(){
		showToastMessage("Error-Could not connect to the server");
	});
}

function selectNewlyAddedState(stateId, stateName){
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
	$("#dropDownStates option[value='"+stateId+"']").prop("selected", true);
	
	fieldStateName.val("").focus();
}

function changeFormStateToSelectedState(){
	buttonAddState.prop("value", "New");
	buttonUpdateState.prop("disabled", false)
	buttonDeleteState.prop("disabled", false)
	
	labelStateName.text("Selected State/province:")
	
	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);
}


function changeFormStateToNew(){
	buttonAddState.val("Add");
	labelStateName.text("State/Province Name:");
	
	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);
	
	fieldStateName.val("").focus();
}


function loadCountries4States(){
	url=contextPath+"countries/list";
	$.get(url, function(responseJSON){
		dropDownCountry4States.empty();
		$.each(responseJSON, function(index, country){
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountry4States);
		});
	}).done(function(){
		buttonLoad4States.val("Refresh countries list");
		showToastMessage("All counties have been loaded");
	}).fail(function(){
		showToastMessage("Error: could not connect to the server");
	});
	
}


function loadStates4Countries(){
	selectedCountry=$("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	url=contextPath+"states/list_by_country/"+countryId;
	
	$.get(url, function(responseJSON){
		dropDownStates.empty();
		$.each(responseJSON, function(index, state){
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});
	}).done(function(){
		changeFormStateToNew();
		showToastMessage("All states have been laoded for the country "+selectedCountry.text());
	}).fail(function(){
		showToastMessage("Error: could not connect to the server");
	});

}



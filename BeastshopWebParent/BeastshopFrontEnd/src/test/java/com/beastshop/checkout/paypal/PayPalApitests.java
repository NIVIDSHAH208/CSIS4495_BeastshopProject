package com.beastshop.checkout.paypal;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PayPalApitests {
	private static final String BASE_URL="https://api.sandbox.paypal.com";
	private static final String GET_ORDER_API="/v2/checkout/orders/";
	private static final String CLIENT_ID = "AXWaML5_j4p0I8eHcjJr1HcbcVGCpSwivgBmIQVV7KCesBj7TxET45dSd87A7MPXhkUd7sbPbiUNXVte";
	private static final String CLIENT_SECRET ="EIxtzcaXn3M8swLZ5yjpkGR1CVzY9LIwCWLRczILbOF24odgfcp10QEum47qLgD0jINKpLhAwARss-s_";
	
	@Test
	public void testGetOrderDetails() {
		String orderId = "21B8739766990791B";
		String requestURL = BASE_URL+GET_ORDER_API+orderId;
		//response in json format
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language","en_US");
		headers.setBasicAuth(CLIENT_ID,CLIENT_SECRET);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<PayPalOrderResponse> response = restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
		PayPalOrderResponse orderResponse = response.getBody();
		System.out.println("Order Id: "+orderResponse.getId());
		System.out.println("Validated: "+orderResponse.validate(orderId));
	}

}

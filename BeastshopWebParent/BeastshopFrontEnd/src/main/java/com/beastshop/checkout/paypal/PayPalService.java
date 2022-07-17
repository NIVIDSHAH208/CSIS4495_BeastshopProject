package com.beastshop.checkout.paypal;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.beastshop.setting.PaymentSettingBag;
import com.beastshop.setting.SettingService;

@Component
public class PayPalService {

	private static final String GET_ORDER_API = "/v2/checkout/orders/";

	@Autowired
	private SettingService settingService;


	public boolean validateOrder(String orderId) throws PayPalApiException {

		PayPalOrderResponse orderResponse = getOrderDetails(orderId);
		return orderResponse.validate(orderId);
	}

	// Method to get the order details
	private PayPalOrderResponse getOrderDetails(String orderId) throws PayPalApiException {
		ResponseEntity<PayPalOrderResponse> response = makeRequest(orderId);
		HttpStatus statusCode = response.getStatusCode();

		if (!statusCode.equals(HttpStatus.OK)) {
			// custom method for handling non ok exceptions
			throwExceptionForNonOKResponse(statusCode);
		}

		return response.getBody();
	}

	private ResponseEntity<PayPalOrderResponse> makeRequest(String orderId) {
		PaymentSettingBag paymentSetting = settingService.getPaymentSetting();
		String baseUrl = paymentSetting.getURL();
		String requestURL = baseUrl + GET_ORDER_API + orderId;
		String clientId = paymentSetting.getClientId();
		String clientSecret = paymentSetting.getClientSecret();

		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Accept-Language", "en_US");
		headers.setBasicAuth(clientId, clientSecret);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.exchange(requestURL, HttpMethod.GET, request, PayPalOrderResponse.class);
	}

	private void throwExceptionForNonOKResponse(HttpStatus statusCode) throws PayPalApiException {
		String message = null;
		switch (statusCode) {
		case NOT_FOUND:
			message = "Order Id not found";
		case BAD_REQUEST:
			message = "Bad request to paypal checkout API";
		case INTERNAL_SERVER_ERROR:
			message = "Paypal Internal server error";
		default:
			message = "Paypal returned non-OK status code";
		}
		throw new PayPalApiException(message);
	}

}

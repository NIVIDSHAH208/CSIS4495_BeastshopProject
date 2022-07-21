package com.beastshop.order;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTests {
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	
	@Test
	@WithUserDetails("nividshah@gmail.com")
	public void testSendOrderReturnRequestFailed() throws Exception {
		Integer orderId = 20;
		
		OrderReturnRequest returnRequest = new OrderReturnRequest(orderId, "","");
		String requestURL = "/orders/return";
		
		mockMvc.perform(post(requestURL).with(csrf())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(returnRequest)))
		.andExpect(status().isNotFound()).andDo(print());
	}
	
	@Test
	@WithUserDetails("nividshah@gmail.com")
	public void testSendOrderReturnRequestSuccessFull() throws Exception {
		Integer orderId = 6;
		String reason = "I bought the wrong item";
		String note ="Please return my money";
		OrderReturnRequest returnRequest = new OrderReturnRequest(orderId, reason, note);
		String requestURL = "/orders/return";
		
		mockMvc.perform(post(requestURL).with(csrf())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(returnRequest)))
		.andExpect(status().isOk()).andDo(print());
	}
}
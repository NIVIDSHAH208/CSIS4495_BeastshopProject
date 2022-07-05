package com.beastshop.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import com.beastshop.admin.setting.country.CountryRepository;
import com.beastshop.common.entity.Country;
import com.beastshop.common.entity.State;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc

public class StateRestControllerTests {
	
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	StateRepository stateRepo;
	@Autowired
	CountryRepository countryRepo;
	
	@Test
	@WithMockUser(username = "nivid@shah.com", password = "nivid2020", roles = "ADMIN")
	public void testListByCountries() throws Exception {
		Integer countryId = 3;
		String url = "/states/list_by_country/"+countryId;
		
		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		State[] states = objectMapper.readValue(jsonResponse, State[].class);
		assertThat(states).hasSizeGreaterThan(0);
	}
	
	@Test
	@WithMockUser(username = "nivid@shah.com", password = "nivid2020", roles = "ADMIN")
	public void testCreateState() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Integer countryId = 4;
		Country country = countryRepo.findById(countryId).get();
		State state = new State("BritishOntario", country);
		
		MvcResult result = mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(state))
				.with(csrf())).andDo(print()).andExpect(status().isOk()).andReturn();
		
		String response = result.getResponse().getContentAsString();
		
		int stateId = Integer.parseInt(response);
		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById.isPresent());
	}
	
	
	@Test
	@WithMockUser(username = "nivid@shah.com", password = "nivid2020", roles = "ADMIN")
	public void testUpdateState() throws Exception {
		String url = "/states/save";
		Integer stateId = 4;
		String stateName = "Ontario";
		
		State state = stateRepo.findById(stateId).get();
		state.setName(stateName);
		
		
		mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(state))
				.with(csrf())).andDo(print()).andExpect(status().isOk()).andExpect(content().string(String.valueOf(stateId)));
		
		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById.isPresent());
		State updatedState = findById.get();
		assertThat(updatedState.getName()).isEqualTo(stateName);
	}
	
	
	@Test
	@WithMockUser(username = "nivid@shah.com", password = "nivid2020", roles = "ADMIN")
	public void testDeleteState() throws Exception {
		Integer stateId = 6;
		String url = "/states/delete/"+stateId;
		
		mockMvc.perform(get(url)).andExpect(status().isOk());
		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById).isNotPresent();
	}

}

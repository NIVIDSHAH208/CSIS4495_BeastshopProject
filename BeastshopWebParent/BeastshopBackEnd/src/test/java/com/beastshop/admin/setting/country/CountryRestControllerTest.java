package com.beastshop.admin.setting.country;

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
//Run entire spring boot project for testing
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.beastshop.common.entity.Country;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc

public class CountryRestControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	CountryRepository repo;

	
	//We have to provide mock user as we have implemented spring security for the back end, so before requesting new page, it must login
	@Test
	@WithMockUser(username = "nivid@shah.com", password = "nivid2020", roles = "ADMIN")
	public void testListCountries() throws Exception {
		String url = "/countries/list";
		MvcResult result = mockMvc.perform(get(url))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		//Read json response from the server and convert to the java object
		String jsonResponse = result.getResponse().getContentAsString();
//		System.out.println(jsonResponse);
		Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
//		for(Country country: countries) {
//			System.out.println(country);
//		}
		assertThat(countries).hasSizeGreaterThan(0);
	}
	
	@Test
	@WithMockUser(username = "nivid@shah.com", password = "nivid2020", roles = "ADMIN")
	public void testCreateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		String countryName = "Germany";
		String countryCode = "Ge";
		Country country = new Country(countryName,countryCode);
		
		//We must send csrf token as required by the spring security in each post request
		MvcResult result = mockMvc.perform(post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(country)).with(csrf()))
		.andDo(print()).andExpect(status().isOk()).andReturn();
		String response = result.getResponse().getContentAsString();
		System.out.println("Country Id: "+response);
		Integer countryId = Integer.parseInt(response);
		
		Country savedCountry = repo.findById(countryId).get();
		
		assertThat(savedCountry.getName()).isEqualTo(countryName);
	
	}
	
	
	@Test
	@WithMockUser(username = "nivid@shah.com", password = "nivid2020", roles = "ADMIN")
	public void testUpdateCountry() throws JsonProcessingException, Exception {
		String url = "/countries/save";
		Integer countryId = 4;
		String countryName = "British Columbia";
		String countryCode = "BC";
		Country country = new Country(countryId, countryName,countryCode);
		
		//We must send csrf token as required by the spring security in each post request
		 mockMvc.perform(post(url)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(country)).with(csrf()))
				.andDo(print())
				.andExpect(status().isOk()).andExpect(content().string(String.valueOf(countryId)));
		
	
		
		Country savedCountry = repo.findById(countryId).get();
		
		assertThat(savedCountry.getName()).isEqualTo(countryName);
	
	}
	
	@Test
	@WithMockUser(username = "nivid@shah.com", password = "nivid2020", roles = "ADMIN")
	public void testDeleteCountry() throws Exception {
		Integer countryId = 5;
		String url = "/countries/delete/"+countryId;
		
		mockMvc.perform(get(url)).andExpect(status().isOk());
		Optional<Country> country = repo.findById(countryId);
		assertThat(country).isNotPresent();
	}
	
}

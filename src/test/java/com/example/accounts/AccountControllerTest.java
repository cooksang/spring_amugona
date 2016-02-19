package com.example.accounts;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.example.AmugonaApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AmugonaApplication.class)
@WebAppConfiguration
@Transactional
public class AccountControllerTest {

	@Autowired
	WebApplicationContext wac;

	@Autowired
	ObjectMapper objectMapper;

	MockMvc mockMvc;
	
	@Autowired
	AccountService service;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void createAccount() throws Exception {
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

		AccountDto.Create createDto = new AccountDto.Create();
		createDto.setUserName("example");
		createDto.setPassword("password");

		ResultActions result = mockMvc.perform(post("/accounts").contentType(
				MediaType.APPLICATION_JSON).content(
				objectMapper.writeValueAsString(createDto)));

		result.andDo(print());
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.userName", equalTo("example")));

		result = mockMvc.perform(post("/accounts").contentType(
				MediaType.APPLICATION_JSON).content(
				objectMapper.writeValueAsString(createDto)));

		result.andDo(print());
		result.andExpect(status().isBadRequest());
//		result.andExpect(jsonPath("$.code", equalTo("bad.request")));
	}

	@Test
	public void createAccount_BadRequest() throws Exception {
		AccountDto.Create createDto = new AccountDto.Create();
		createDto.setUserName(" ");
		createDto.setPassword("1234");

		ResultActions result = mockMvc.perform(post("/accounts").contentType(
				MediaType.APPLICATION_JSON).content(
				objectMapper.writeValueAsString(createDto)));

		result.andDo(print());
		result.andExpect(status().isBadRequest());
	}

	@Test
	public void getAccounts() throws Exception{
		AccountDto.Create createDto = accountCreateDto();
		service.createAccount(createDto);
		
		ResultActions result = mockMvc.perform(get("/accounts"));
		
		result.andDo(print());
		result.andExpect(status().isOk());
	}
	
	private AccountDto.Create accountCreateDto() {
		AccountDto.Create createDto = new AccountDto.Create();
		createDto.setUserName("example");
		createDto.setPassword("password");

		return createDto;
	}
	
	@Test
	public void getAccount() throws Exception{
		AccountDto.Create createDto = accountCreateDto();
		Account account = service.createAccount(createDto);

		ResultActions result = mockMvc.perform(get("/accounts/" + account.getId()));
		
		result.andDo(print());
		result.andExpect(status().isOk());
	}
	
	@Test
	public void updateAccount() throws Exception{
		AccountDto.Create createDto = accountCreateDto();
		Account account = service.createAccount(createDto);
		
		AccountDto.Update updateDto = new AccountDto.Update();
		updateDto.setFullName("exampleUpdate");
		updateDto.setPassword("pass");
		
		ResultActions result = mockMvc.perform(put("/accounts/" + account.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto)));
		
		result.andDo(print());
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.fullName", equalTo("exampleUpdate")));
	}
	
	@Test
	public void deleteAccount() throws Exception{
		ResultActions result = mockMvc.perform(delete("/accounts/1"));
		result.andDo(print());
		result.andExpect(status().isBadRequest());
		
		AccountDto.Create createDto = accountCreateDto();
		Account account = service.createAccount(createDto);

		result = mockMvc.perform(delete("/accounts/" + account.getId()));
		result.andDo(print());
		result.andExpect(status().isNoContent());
	}
}

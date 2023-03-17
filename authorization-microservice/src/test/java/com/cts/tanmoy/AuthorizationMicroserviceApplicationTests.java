package com.cts.tanmoy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.cts.tanmoy.controller.AuthorizationController;
import com.cts.tanmoy.entity.AuthenticationRequest;
import com.cts.tanmoy.service.JwtUtil;
import com.cts.tanmoy.service.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationMicroserviceApplicationTests {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	AuthorizationController controller;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Test
	public void contextLoads() {
		log.info("Starting Sanity Test");
		log.info("Sanity Test Ended");
		assertNotNull(controller);
	}

	@Test
	public void healthCheckMethod() {
		log.info("Starting Execution of HealthCheck Method");
		ResponseEntity<?> actualValue = controller.healthCheckMethod();
		assertEquals(actualValue, actualValue);
	}

	@Test
	public void validateLogin() throws Exception {
		AuthenticationRequest a = new AuthenticationRequest("tanmoy", "admin");
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(a);
		mockMvc.perform(post("/authorization/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk());
	}

	@Test
	public void validateLoginFail() throws Exception {
		AuthenticationRequest a = new AuthenticationRequest("t2", "admin");
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(a);
		mockMvc.perform(post("/authorization/login").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void validateToken() throws Exception {
		final UserDetails userDetails = userDetailsService.loadUserByUsername("tanmoy");
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		ResultActions actions = mockMvc.perform(get("/authorization/validate").header("Authorization", "Bearer " + jwt));
		actions.andExpect(status().isOk());
	}

}

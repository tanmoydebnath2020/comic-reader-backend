package com.cts.tanmoy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.tanmoy.dto.AuthorizationResponse;
import com.cts.tanmoy.entity.AuthenticationRequest;
import com.cts.tanmoy.entity.AuthenticationResponse;
import com.cts.tanmoy.service.JwtUtil;
import com.cts.tanmoy.service.MyUserDetailsService;

@RestController
@RequestMapping("/authorization")
@CrossOrigin(origins = "http://localhost:8400")
public class AuthorizationController {

	Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;


	@GetMapping("/get-health")
	public ResponseEntity<?> healthCheckMethod() {
		logger.debug("Health check method");
		return ResponseEntity.ok("Health check successful");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authRequest) {
		logger.info("START");
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		} catch (Exception e) {
			logger.info("User is not validated hence not Authorized");
			return new ResponseEntity<>("Invalid username/password", HttpStatus.UNAUTHORIZED);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		logger.info("User is logged in and authorized");
		logger.info("END");
		return ResponseEntity.ok(new AuthenticationResponse(authRequest.getUsername(), jwt));
	}

	@GetMapping(value = "/validate")
	public ResponseEntity<AuthorizationResponse> getValidation(@RequestHeader("Authorization") String token) {
		final String token1 = token.substring(7);
		AuthorizationResponse auth = new AuthorizationResponse();
		logger.info("Token validation for ", jwtTokenUtil.extractUsername(token1));
		if (!jwtTokenUtil.isTokenExpired(token1)) {
			logger.info("Token validated");
			auth.setValidStatus(true);
		}
		return new ResponseEntity<>(auth, HttpStatus.OK);
	}

}

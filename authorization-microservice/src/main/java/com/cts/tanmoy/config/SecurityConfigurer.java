package com.cts.tanmoy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.cts.tanmoy.service.JwtRequestFilter;
import com.cts.tanmoy.service.MyUserDetailsService;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@EnableWebMvc
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	public static final String[] public_urls= {
            "/login",
            "/h2-console/**",
            "/get-health",
            "/swagger-resources/",
            "/swagger-ui/",
            "/webjars/**"
    };

	@Autowired
	MyUserDetailsService userDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers(public_urls).permitAll()
		.antMatchers("/v3/api-docs/**").permitAll()
		.antMatchers("/swagger-ui/**").permitAll()
		.antMatchers("/authorization/**").permitAll()
		.antMatchers("/authorization/login").permitAll()
		.antMatchers("/authorization/login**").permitAll()
		.anyRequest().authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**",
//				"/configuration/security", "/swagger-ui.html", "/webjars/**","/h2-console/**");
//	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	PasswordEncoder passwordEncoder() {

		return NoOpPasswordEncoder.getInstance();

	}

}

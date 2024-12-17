package com.ecommerce.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class AppConfig {
	// csrf disable as we are using tokens for stateless authentication.
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authorize -> authorize.requestMatchers("*/api/**").authenticated()
						.requestMatchers(("*/api/products/*/reviews")).permitAll().anyRequest().permitAll())
				.addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class).csrf(csrf -> csrf.disable())
				.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		return http.build();

	}

	private CorsConfigurationSource corsConfigurationSource() {
		return new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				// Create a new CorsConfiguration instance
				CorsConfiguration cfg = new CorsConfiguration();

				// Replace "*" with the specific frontend URL(s)
				cfg.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // Allow this specific origin

				// Allow all methods (GET, POST, PUT, DELETE, etc.)
				cfg.setAllowedMethods(Collections.singletonList("*"));

				// Allow all headers
				cfg.setAllowedHeaders(Collections.singletonList("*"));

				// Allow credentials to be included in the request
				cfg.setAllowCredentials(true);

				// Expose the Authorization header in responses
				cfg.setExposedHeaders(Collections.singletonList("Authorization"));

				// Set the maximum age of the CORS pre-flight request cache
				cfg.setMaxAge(3600L);

				return cfg;
			}
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// to use external api we use rest template
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

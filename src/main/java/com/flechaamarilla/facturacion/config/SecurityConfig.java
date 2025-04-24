package com.flechaamarilla.facturacion.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {


	@Autowired
	private GeneralConfig generalConfig;

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		//if(generalConfig.isTest()) { // Is Developer mode?

			http
					.csrf().disable()
					.cors().and()
					.authorizeHttpRequests()
					.antMatchers("/**").permitAll();

//		} else { // Production mode
//
//			http
//					.csrf().disable()
//					.cors().and()
//					.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//
//					// Permitir acceso a Swagger UI y sus recursos sin autenticación
//					.authorizeRequests()
//					.antMatchers("/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/**", "/")
//					.permitAll().and()
//
//					// Para otras URLs
//					.authorizeRequests()
//					.antMatchers(
//							"/",
//							"/**/*.ico",
//							"/**/*.png",
//							"/**/*.gif",
//							"/**/*.svg",
//							"/**/*.jpg",
//							"/**/*.html",
//							"/**/*.css",
//							"/**/*.js",
//							"/**/*.ttf",
//							"/**/*.woff2"
//					)
//					.permitAll()
//
//|
//					.anyRequest().authenticated();
//		}

		return http.build();
	}
}
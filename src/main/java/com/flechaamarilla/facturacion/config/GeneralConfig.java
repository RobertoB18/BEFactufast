package com.flechaamarilla.facturacion.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "general")
public class GeneralConfig {

	private String applicationName;
	private boolean isTest;
	private String facturapiToken;

	private ApiConfig prodApi;
	private ApiConfig testApi;

	@Getter
	@Setter
	public static class ApiConfig {
		private String facturapi;
	}

	public String getFacturapi() {
		return isTest ? testApi.getFacturapi() : prodApi.getFacturapi();
	}
}

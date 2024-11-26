package com.jaefan.munpyspring.common.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.bonigarcia.wdm.WebDriverManager;

@Configuration
public class AppConfig {

	@Bean
	public WebDriver webDriver() {
		WebDriverManager.chromedriver().browserVersion("130.0.6723.92").setup();
		return new ChromeDriver();
	}
}

package com.jaefan.munpyspring.common.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

	/**
	 * 현재 인스턴스에서 크롬을 실행하려면
	 * 컨테이너나 서버 환경에서는 GUI가 없으므로, Chrome을 headless 모드로 실행해야 함
	 * 작성자 : 냄규
	 */
	@Bean
	public WebDriver webDriver() {
		WebDriverManager.chromedriver().setup(); // 버전 불일치로 수정 했습니다. 혹시라도 잘못 되면 노티 주세요!!

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");             // GUI 없이 실행
		options.addArguments("--disable-gpu");          // GPU 사용 비활성화
		options.addArguments("--no-sandbox");           // 샌드박스 모드 비활성화
		options.addArguments("--disable-dev-shm-usage");  // /dev/shm 사용 제한 문제 해결

		return new ChromeDriver(options);
	}

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}
}

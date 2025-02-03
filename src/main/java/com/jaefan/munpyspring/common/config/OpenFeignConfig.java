package com.jaefan.munpyspring.common.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * EnableFeignClients에 선언된 경로부터 @FeignClient가 붙은 인터페이스들을 스프링 빈 등록하는 configuration.
 * 구현하신 @FeignClient의 경로를 basePackages에 추가하시면 빈 등록 됩니다.
 */
@Configuration
@EnableFeignClients(basePackages = {
	"com.jaefan.munpyspring.user.presentation.feignClient",
	"com.jaefan.munpyspring.shelter.presentation.feignClient"
})
public class OpenFeignConfig {

}

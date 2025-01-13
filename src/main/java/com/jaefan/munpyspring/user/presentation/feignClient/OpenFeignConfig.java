package com.jaefan.munpyspring.user.presentation.feignClient;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * EnableFeignClients에 선언된 경로부터 @FeignClient가 붙은 인터페이스들을 스프링 빈 등록하는 configuration.
 */
@Configuration
@EnableFeignClients("com.jaefan.munpyspring.user.presentation.feignClient")
public class OpenFeignConfig {

}

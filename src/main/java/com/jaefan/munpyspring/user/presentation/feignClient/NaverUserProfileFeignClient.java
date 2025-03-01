package com.jaefan.munpyspring.user.presentation.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 네이버 리소스 서버로 사용자 정보 요청을 보내는 객체 (요청에 토큰 포함)
 */
@FeignClient(name = "NaverUserProfileFeignClient", url = "https://openapi.naver.com")
public interface NaverUserProfileFeignClient {
	@PostMapping(value = "/v1/nid/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	ResponseEntity<String> getKakaoUserProfile(
		@RequestHeader("Authorization") String authorization // 토큰
	);
}

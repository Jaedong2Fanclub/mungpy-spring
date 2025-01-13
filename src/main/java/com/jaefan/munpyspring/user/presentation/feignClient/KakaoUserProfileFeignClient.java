package com.jaefan.munpyspring.user.presentation.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 카카오 리소스 서버로 사용자 정보 요청을 보내는 객체 (요청에 토큰 포함)
 */
@FeignClient(name = "KakaoUserProfileFeignClient", url = "https://kapi.kakao.com")
public interface KakaoUserProfileFeignClient {
	@PostMapping(value = "/v2/user/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	ResponseEntity<String> getKakaoUserProfile(
		@RequestHeader("Authorization") String authorization, // 인증 토큰
		@RequestParam("property_keys") String propertyKeys // 받아올 사용자 정보들 (email, profileImage)
	);
}

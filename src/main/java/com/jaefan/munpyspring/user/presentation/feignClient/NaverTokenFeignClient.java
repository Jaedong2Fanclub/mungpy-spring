package com.jaefan.munpyspring.user.presentation.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jaefan.munpyspring.user.presentation.dto.OAuthTokenDto;

/**
 * 네이버 인증 서버로 토큰 발급 요청을 보내는 객체
 */
@FeignClient(name = "NaverTokenFeignClient", url = "https://nid.naver.com")
public interface NaverTokenFeignClient {
	@PostMapping(value = "/oauth2.0/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	OAuthTokenDto getNaverTokenByCode(
		@RequestParam("grant_type") String grant_type,
		@RequestParam("client_id") String client_id,
		@RequestParam("client_secret") String client_secret,
		@RequestParam("code") String code,
		@RequestParam("state") String state
	);
}

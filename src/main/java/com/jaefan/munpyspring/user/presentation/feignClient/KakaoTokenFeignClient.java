package com.jaefan.munpyspring.user.presentation.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jaefan.munpyspring.user.presentation.dto.OAuthToken;

/**
 * 카카오 인증 서버로 토큰 발급 요청을 보내는 객체
 */
@FeignClient(name = "KakaoTokenFeignClient", url = "https://kauth.kakao.com")
public interface KakaoTokenFeignClient {
	@PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	OAuthToken getKakaoTokenByCode(
		@RequestParam("grant_type") String grantType,
		@RequestParam("client_id") String clientId,
		@RequestParam("redirect_uri") String redirectUri,
		@RequestParam("code") String code
	);
}

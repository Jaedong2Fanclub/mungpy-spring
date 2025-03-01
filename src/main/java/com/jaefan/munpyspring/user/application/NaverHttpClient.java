package com.jaefan.munpyspring.user.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaefan.munpyspring.user.presentation.dto.OAuthAccountDto;
import com.jaefan.munpyspring.user.presentation.dto.OAuthTokenDto;
import com.jaefan.munpyspring.user.presentation.exception.OAuthProcessingException;
import com.jaefan.munpyspring.user.presentation.feignClient.NaverTokenFeignClient;
import com.jaefan.munpyspring.user.presentation.feignClient.NaverUserProfileFeignClient;

import lombok.RequiredArgsConstructor;

/**
 * FeignClient를 사용해 HTTP 요청을 대리 수행해주는 컴포넌트
 */
@Component
@RequiredArgsConstructor
public class NaverHttpClient {

	@Value("${oauth2.naver.api.client-id}")
	private String clientId;

	@Value("${oauth2.naver.api.client-secret}")
	private String clientSecret;

	@Value("${oauth2.naver.api.redirect-uri}")
	private String redirectUri;

	private final ObjectMapper objectMapper;

	private final NaverTokenFeignClient naverTokenFeignClient; // 토큰발급 요청을 보내는 FeignClient

	private final NaverUserProfileFeignClient naverUserProfileFeignClient; // 프로필 정보 요청을 보내는 FeignClient

	public OAuthTokenDto getTokenByCode(String code, String state) {
		try {
			return naverTokenFeignClient.getNaverTokenByCode(
				"authorization_code",
				clientId,
				clientSecret,
				code,
				state
			);
		} catch (Exception ex) {
			throw new OAuthProcessingException("Failed to process Naver OAuth");
		}
	}

	public OAuthAccountDto getAccountByOAuthToken(String authorization) {
		try {
			ResponseEntity<String> profileResponse = naverUserProfileFeignClient.getKakaoUserProfile(authorization);

			JsonNode rootNode = objectMapper.readTree(profileResponse.getBody());
			JsonNode responseNode = rootNode.path("response");
			String profileImageUrl = responseNode.path("profile_image").asText();
			String email = responseNode.path("email").asText();

			return new OAuthAccountDto(profileImageUrl, email);
		} catch (Exception ex) {
			throw new OAuthProcessingException("Failed to process Naver OAuth");
		}
	}
}

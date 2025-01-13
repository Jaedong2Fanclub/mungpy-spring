package com.jaefan.munpyspring.user.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaefan.munpyspring.user.presentation.dto.OAuthAccountDto;
import com.jaefan.munpyspring.user.presentation.dto.OAuthToken;
import com.jaefan.munpyspring.user.presentation.exception.OAuthProcessingException;
import com.jaefan.munpyspring.user.presentation.feignClient.KakaoTokenFeignClient;
import com.jaefan.munpyspring.user.presentation.feignClient.KakaoUserProfileFeignClient;

import lombok.RequiredArgsConstructor;

/**
 * FeignClient를 사용해 HTTP 요청을 대리 수행해주는 컴포넌트
 */
@Component
@RequiredArgsConstructor
public class KakaoHttpClient {

	@Value("${oauth2.kakao.api.client-id}")
	private String clientId;

	@Value("${oauth2.kakao.api.redirect-uri}")
	private String redirectUri;

	private final ObjectMapper objectMapper;

	private final KakaoTokenFeignClient kakaoTokenFeignClient; // 토큰발급 요청을 보내는 FeignClient

	private final KakaoUserProfileFeignClient kakaoUserProfileFeignClient; // 프로필 정보 요청을 보내는 FeignClient

	public OAuthToken getTokenByCode(String code) {
		try {
			return kakaoTokenFeignClient.getKakaoTokenByCode(
				"authorization_code", // 고정값
				clientId,
				redirectUri,
				code
			);
		} catch (Exception ex) {
			throw new OAuthProcessingException("Failed to process Kakao OAuth");
		}
	}

	public OAuthAccountDto getAccountByOAuthToken(String authorization, String propertyKeys) {
		try {
			ResponseEntity<String> profileResponse = kakaoUserProfileFeignClient.getKakaoUserProfile(
				authorization,
				propertyKeys
			);

			JsonNode rootNode = objectMapper.readTree(profileResponse.getBody());
			JsonNode profileNode = rootNode.path("kakao_account").path("profile");
			String profileImageUrl = profileNode.path("profile_image_url").asText();
			String email = rootNode.path("kakao_account").path("email").asText();

			return new OAuthAccountDto(profileImageUrl, email);
		} catch (Exception ex) {
			throw new OAuthProcessingException("Failed to process Kakao OAuth");
		}
	}
}

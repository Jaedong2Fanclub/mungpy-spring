package com.jaefan.munpyspring.user.application;

import org.springframework.stereotype.Service;

import com.jaefan.munpyspring.user.presentation.dto.OAuthAccountDto;
import com.jaefan.munpyspring.user.presentation.dto.OAuthTokenDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuthProvider {

	private final KakaoHttpClient kakaoHttpClient; // HTTP 요청, 응답을 대리 수행해주는 객체

	public OAuthTokenDto getTokenByCode(String code) {
		return kakaoHttpClient.getTokenByCode(code);
	}

	public OAuthAccountDto getAccountByOAuthToken(OAuthTokenDto oAuthTokenDto) {
		String authorization = "Bearer " + oAuthTokenDto.getAccess_token();
		String propertyKeys = "[\"kakao_account.email\", \"kakao_account.profile\"]";

		return kakaoHttpClient.getAccountByOAuthToken(authorization, propertyKeys);
	}
}

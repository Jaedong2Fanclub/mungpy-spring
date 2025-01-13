package com.jaefan.munpyspring.user.application;

import org.springframework.stereotype.Service;

import com.jaefan.munpyspring.user.presentation.dto.OAuthAccountDto;
import com.jaefan.munpyspring.user.presentation.dto.OAuthToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NaverOAuthProvider {

	private final NaverHttpClient naverHttpClient; // HTTP 요청, 응답을 대리 수행해주는 객체

	public OAuthToken getTokenByCode(String code, String state) {
		return naverHttpClient.getTokenByCode(code, state);
	}

	public OAuthAccountDto getAccountByOAuthToken(OAuthToken oAuthToken) {
		String authorization = "Bearer " + oAuthToken.getAccess_token();

		return naverHttpClient.getAccountByOAuthToken(authorization);
	}
}

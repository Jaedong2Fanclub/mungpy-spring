package com.jaefan.munpyspring.user.presentation.dto;

import lombok.Getter;

/**
 * OAuth Token으로 사용자 정보인 OAuthAccountDto를 받기 위해 필요한 토큰
 */
@Getter
public class OAuthTokenDto {

	private String token_type;

	private String access_token;

	private String refresh_token;

	private int expires_in;

	private int refresh_token_expires_in;
}

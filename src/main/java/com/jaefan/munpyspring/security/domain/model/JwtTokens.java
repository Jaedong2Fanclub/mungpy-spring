package com.jaefan.munpyspring.security.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokens {
	private String accessToken; // 유효시간: 30분
	private String refreshToken; // 유효시간: 2주
}

package com.jaefan.munpyspring.user.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 액세스 토큰 재발급을 위한 리프래쉬 토큰 Dto
 */
@Getter
@NoArgsConstructor
public class RefreshTokenRequest {
	private String refreshToken;
}

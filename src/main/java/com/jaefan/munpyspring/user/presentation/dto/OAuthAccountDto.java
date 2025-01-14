package com.jaefan.munpyspring.user.presentation.dto;

import static com.jaefan.munpyspring.user.domain.model.Status.*;

import java.time.LocalDateTime;
import java.util.UUID;

import com.jaefan.munpyspring.user.domain.model.Provider;
import com.jaefan.munpyspring.user.domain.model.Role;
import com.jaefan.munpyspring.user.domain.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OAuth 계정 정보를 포함한 Dto
 */
@Getter
@AllArgsConstructor
public class OAuthAccountDto {

	private String profileImage; // url

	private String email;

	public static User toUser(OAuthAccountDto oAuthAccountDto, String nickname, Provider providerType, Role role) {
		return User.builder()
			.uuid(UUID.randomUUID())
			.nickname(nickname)
			.profileImage(oAuthAccountDto.getProfileImage())
			.providerType(providerType)
			.email(oAuthAccountDto.getEmail())
			.status(ACTIVE)
			.password("")
			.createdAt(LocalDateTime.now())
			.role(role)
			.build();
	}
}

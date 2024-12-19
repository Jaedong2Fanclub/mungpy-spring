package com.jaefan.munpyspring.user.domain.model;

import java.time.LocalDateTime;

import com.jaefan.munpyspring.animal.domain.model.ProtectionStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nickname;

	private String s3Url;

	// private Provider providerType; SignUp 기능 구현 필요

	private String email;

	private ProtectionStatus protectionStatus;

	private LocalDateTime createdAt;

	private LocalDateTime visitedAt;

	// private Role role; SignUp 기능 구현 필요
}

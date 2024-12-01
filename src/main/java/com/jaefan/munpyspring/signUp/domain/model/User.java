package com.jaefan.munpyspring.signUp.domain.model;

import java.time.LocalDateTime;

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

	private Provider providerType;

	private String email;

	private Status status;

	private LocalDateTime createdAt;

	private LocalDateTime visitedAt;

	private Role role;
}

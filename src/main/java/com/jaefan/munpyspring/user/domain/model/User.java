package com.jaefan.munpyspring.user.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", indexes = {
	@Index(name = "idx_uuid", columnList = "uuid"),
	@Index(name = "idx_email", columnList = "email")
})
public class User {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private UUID uuid;

	@Column(length = 50, nullable = false)
	private String nickname;

	@Column(name = "profile_image")
	private String profileImage;

	@Enumerated(EnumType.STRING)
	@Column(name = "provider_type", nullable = false)
	private Provider providerType;

	@Column(nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@Column(nullable = false)
	private String password;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "visited_at")
	private LocalDateTime visitedAt;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	public void updateLastVisit() {
		this.visitedAt = LocalDateTime.now();
	}

	public void updateProfileImage(String imageUrl) {
		this.profileImage = imageUrl;
	}

	public void hashPassword(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
	}
}

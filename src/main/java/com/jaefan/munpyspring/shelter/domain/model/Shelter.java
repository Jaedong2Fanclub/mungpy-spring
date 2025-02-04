package com.jaefan.munpyspring.shelter.domain.model;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.jaefan.munpyspring.region.domain.model.Region;
import com.jaefan.munpyspring.user.domain.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shelter {
	@Id
	@Column(name = "shelter_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 150, nullable = false)
	private String name;

	@Column(length = 300, nullable = false)
	private String address;

	@Column(length = 13, nullable = false)
	private String telNo;

	private Double latitude;

	private Double longitude;

	@NotNull
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "region_id")
	private Region region;

	public void hashPassword(PasswordEncoder passwordEncoder) {
		user.hashPassword(passwordEncoder);
	}
}

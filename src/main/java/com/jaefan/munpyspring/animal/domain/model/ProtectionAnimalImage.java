package com.jaefan.munpyspring.animal.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProtectionAnimalImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "protection_animal_id")
	private ProtectionAnimal protectionAnimal;

	public ProtectionAnimalImage(String imageUrl, ProtectionAnimal protectionAnimal) {
		this.imageUrl = imageUrl;
		this.protectionAnimal = protectionAnimal;
	}
}

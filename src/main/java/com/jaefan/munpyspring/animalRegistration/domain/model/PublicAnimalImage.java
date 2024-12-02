package com.jaefan.munpyspring.animalRegistration.domain.model;

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
public class PublicAnimalImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String s3Url;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publicAnimal_id")
	private PublicAnimal publicAnimal;

	public PublicAnimalImage(String s3Url, PublicAnimal publicAnimal) {
		this.s3Url = s3Url;
		this.publicAnimal = publicAnimal;
	}
}

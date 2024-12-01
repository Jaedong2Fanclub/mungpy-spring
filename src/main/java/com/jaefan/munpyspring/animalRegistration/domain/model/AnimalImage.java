package com.jaefan.munpyspring.animalRegistration.domain.model;

import jakarta.persistence.Entity;
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
public class AnimalImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String s3Url;

	@ManyToOne
	@JoinColumn(name = "animal_id")
	private Animal animal;

	public AnimalImage(String s3Url, Animal animal) {
		this.s3Url = s3Url;
		this.animal = animal;
	}
}

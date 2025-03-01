package com.jaefan.munpyspring.animal.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Breed {
	@Id
	@Column(name = "breed_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String family;
	private String breedName;
	private String breedDescription;
	private String imagePath;
}

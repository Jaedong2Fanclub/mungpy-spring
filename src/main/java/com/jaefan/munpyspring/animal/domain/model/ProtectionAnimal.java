package com.jaefan.munpyspring.animal.domain.model;

import java.time.LocalDateTime;

import com.jaefan.munpyspring.animal.presentation.dto.AnimalSearchResponseDto;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProtectionAnimal {
	@Id
	@Column(name = "protection_animal_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private AnimalType type;

	@Enumerated(EnumType.STRING)
	private AnimalGender gender;

	@Enumerated(EnumType.STRING)
	private AnimalNeutered isNeutered;

	private Boolean caution;

	private String noticeNo;

	private String age;

	private double weight;

	@Enumerated(EnumType.STRING)
	private ProtectionStatus protectionStatus;

	private LocalDateTime announcedAt;

	private LocalDateTime exitAt;

	private String rescuePlace;

	private String rescueDetail;

	private String medicalCheck;

	private String vaccination;

	@ManyToOne
	@JoinColumn(name = "breed_id")
	private Breed breed;

	@ManyToOne
	@JoinColumn(name = "shelter_id")
	private Shelter shelter;

	public AnimalSearchResponseDto toResponseDto() {
		String gender = switch (this.gender) {
			case MALE -> "수컷";
			case FEMALE -> "암컷";
			case UNKNOWN -> "미상";
		};

		return AnimalSearchResponseDto.builder()
			.gender(gender)
			.age(this.age)
			.isNeutered(this.isNeutered)
			.weight(this.weight)
			.rescuePlace(this.rescuePlace)
			.rescueDetail(this.rescueDetail)
			.breedName(this.breed.getBreedName())
			.ownerName(this.shelter.getOwner())
			.shelterName(this.shelter.getName())
			.telno(this.shelter.getTelNo())
			.build();
	}

	public void closeProtection() {
		this.protectionStatus = ProtectionStatus.RELEASED;
	}
}

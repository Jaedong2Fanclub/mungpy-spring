package com.jaefan.munpyspring.animalinfo.domain.model;

import java.time.LocalDateTime;

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
public class PublicAnimal {
	@Id
	@Column(name = "public_animal_id")
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

	@Enumerated(EnumType.STRING)
	private ProtectionStatus protectionStatus;

	private LocalDateTime exitAt;

	private LocalDateTime rescuedAt;

	private String rescuePlace;

	private String rescueReason;

	private LocalDateTime dueAt;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "shelter_id")
	private Shelter shelter;

	public void closeAnnouncement() {
		this.protectionStatus = ProtectionStatus.PROTECTED;
	}

	public void closeProtection() {
		this.protectionStatus = ProtectionStatus.RELEASED;
	}
}

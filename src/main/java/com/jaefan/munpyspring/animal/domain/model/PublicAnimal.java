package com.jaefan.munpyspring.animal.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.jaefan.munpyspring.shelter.domain.model.Shelter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "public_animals")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "breed_id")
	private Breed breed;

	@Setter
	@OneToMany(mappedBy = "publicAnimal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PublicAnimalImage> publicAnimalImages;

	public PublicAnimal(
		AnimalType type, AnimalGender gender, AnimalNeutered isNeutered, Boolean caution,
		String noticeNo, LocalDateTime rescuedAt, String rescuePlace,
		String rescueReason, Shelter shelter, Breed breed
	) {
		this.type = type;
		this.gender = gender;
		this.isNeutered = isNeutered;
		this.caution = caution;
		this.noticeNo = noticeNo;
		this.protectionStatus = ProtectionStatus.POSTED;
		this.rescuedAt = rescuedAt;
		this.rescuePlace = rescuePlace;
		this.rescueReason = rescueReason;
		this.dueAt = LocalDateTime.now().plusDays(10);
		this.shelter = shelter;
		this.breed = breed;
	}

	public void closeAnnouncement() {
		this.protectionStatus = ProtectionStatus.PROTECTED;
	}

	public void closeProtection() {
		this.protectionStatus = ProtectionStatus.RELEASED;
	}
}

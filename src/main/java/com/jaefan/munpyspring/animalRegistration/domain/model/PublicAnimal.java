package com.jaefan.munpyspring.animalRegistration.domain.model;

import static com.jaefan.munpyspring.animalRegistration.domain.model.ProtectionStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import com.jaefan.munpyspring.signUp.domain.model.Shelter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class PublicAnimal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type;

	private String gender;

	private String isNeutered;

	private Boolean caution;

	private String noticeNo;

	private ProtectionStatus protectionStatus;

	private LocalDateTime exitAt;

	@Setter
	@OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<PublicAnimalImage> publicAnimalImages;

	private LocalDateTime rescuedAt;

	private String rescuePlace;

	private String rescueReason;

	private LocalDateTime dueAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shelter_id")
	private Shelter shelter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "breed_id")
	private Breed breed;

	public PublicAnimal(
		String type, String gender, String isNeutered, Boolean caution,
		String noticeNo, LocalDateTime rescuedAt, String rescuePlace,
		String rescueReason, Shelter shelter, Breed breed
	) {
		this.type = type;
		this.gender = gender;
		this.isNeutered = isNeutered;
		this.caution = caution;
		this.noticeNo = noticeNo;
		this.protectionStatus = ENTER;
		this.rescuedAt = rescuedAt;
		this.rescuePlace = rescuePlace;
		this.rescueReason = rescueReason;
		this.dueAt = LocalDateTime.now().plusDays(10);
		this.shelter = shelter;
		this.breed = breed;
	}
}

package com.jaefan.munpyspring.animalRegistration.model;

import java.time.LocalDateTime;
import java.util.List;

import com.jaefan.munpyspring.signUp.model.Shelter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
public class Animal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String type;

	private String gender;

	private String isNeutered;

	private Boolean caution;

	private String noticeNo;

	private Status status;

	private LocalDateTime exitAt;

	@Setter
	@OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
	private List<AnimalImage> animalImages;

	private LocalDateTime rescuedAt;

	private String rescuePlace;

	private String rescueReason;

	private LocalDateTime dueAt;

	@ManyToOne
	@JoinColumn(name = "shelter_id")
	private Shelter shelter;

	@ManyToOne
	@JoinColumn(name = "breed_id")
	private Breed breed;

	public Animal(
		String type, String gender, String isNeutered, Boolean caution,
		String noticeNo, Status status, LocalDateTime exitAt,
		LocalDateTime rescuedAt, String rescuePlace, String rescueReason,
		LocalDateTime dueAt, Shelter shelter, Breed breed
	) {
		this.type = type;
		this.gender = gender;
		this.isNeutered = isNeutered;
		this.caution = caution;
		this.noticeNo = noticeNo;
		this.status = status;
		this.exitAt = exitAt;
		this.rescuedAt = rescuedAt;
		this.rescuePlace = rescuePlace;
		this.rescueReason = rescueReason;
		this.dueAt = dueAt;
		this.shelter = shelter;
		this.breed = breed;
	}
}
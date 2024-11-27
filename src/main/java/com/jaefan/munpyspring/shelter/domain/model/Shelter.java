package com.jaefan.munpyspring.shelter.domain.model;

import java.util.List;

import com.jaefan.munpyspring.animalinfo.domain.model.PublicAnimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class Shelter {
	@Id
	@Column(name = "shelter_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String address;

	private String owner;

	private String telno;

	@OneToMany(mappedBy = "shelter")
	private List<PublicAnimal> publicAnimals;
}

package com.jaefan.munpyspring.shelter.domain.repository;

import java.util.List;

import com.jaefan.munpyspring.animal.domain.model.AnimalGender;
import com.jaefan.munpyspring.animal.domain.model.AnimalType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AnimalSearchCondition {

	private List<Long> shelterIds;
	private AnimalType animalType;
	private String breedType;
	private AnimalGender gender;
}

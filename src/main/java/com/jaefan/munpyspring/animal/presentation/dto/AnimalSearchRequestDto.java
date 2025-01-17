package com.jaefan.munpyspring.animal.presentation.dto;

import com.jaefan.munpyspring.animal.domain.model.AnimalGender;
import com.jaefan.munpyspring.animal.domain.model.AnimalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalSearchRequestDto {

	private String upperRegion;
	private String lowerRegion;
	private String shelterName;
	private AnimalType animalType;
	private String breedType;
	private AnimalGender gender;
}

package com.jaefan.munpyspring.animal.presentation.dto;

import com.jaefan.munpyspring.animal.domain.model.AnimalNeutered;

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
public class AnimalSearchResponseDto {

	private String gender;
	private String age;
	private AnimalNeutered isNeutered;
	private double weight;
	private String rescuePlace;
	private String rescueDetail;
	private String breedName;
	private String ownerName;
	private String shelterName;
	private String telno;
}

package com.jaefan.munpyspring.animal.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jaefan.munpyspring.animal.domain.model.AnimalType;
import com.jaefan.munpyspring.animal.domain.model.Breed;
import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;
import com.jaefan.munpyspring.animal.domain.repository.BreedRepository;
import com.jaefan.munpyspring.animal.domain.repository.ProtectionAnimalRepository;
import com.jaefan.munpyspring.animal.presentation.dto.AnimalSearchRequestDto;
import com.jaefan.munpyspring.animal.presentation.dto.AnimalSearchResponseDto;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.jaefan.munpyspring.animal.domain.repository.AnimalSearchCondition;
import com.jaefan.munpyspring.shelter.domain.repository.ShelterRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalSearchService {

	private final BreedRepository breedRepository;
	private final ShelterRepository shelterRepository;
	private final ProtectionAnimalRepository protectionAnimalRepository;

	public List<String> findBreedNames(AnimalType animalType) {
		return breedRepository.findByFamily(animalType.name()).stream()
			.map(Breed::getBreedName)
			.toList();
	}

	public List<AnimalSearchResponseDto> findAnimals(AnimalSearchRequestDto animalSearchRequestDto) {
		String upper = animalSearchRequestDto.getUpperRegion();
		String lower = animalSearchRequestDto.getLowerRegion();
		List<Long> shelterIds = shelterRepository.findByRegion(upper, lower).stream()
			.map(Shelter::getId)
			.toList();

		AnimalSearchCondition animalSearchCondition = AnimalSearchCondition.builder()
			.shelterIds(shelterIds)
			.animalType(animalSearchRequestDto.getAnimalType())
			.breedType(animalSearchRequestDto.getBreedType())
			.gender(animalSearchRequestDto.getGender())
			.build();

		return protectionAnimalRepository.findProtectionAnimals(animalSearchCondition).stream()
			.map(ProtectionAnimal::toResponseDto)
			.toList();
	}
}

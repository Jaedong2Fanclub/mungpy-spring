package com.jaefan.munpyspring.animal.application;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jaefan.munpyspring.animal.domain.model.AnimalType;
import com.jaefan.munpyspring.animal.domain.model.Breed;
import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;
import com.jaefan.munpyspring.animal.domain.repository.AnimalSearchCondition;
import com.jaefan.munpyspring.animal.domain.repository.BreedRepository;
import com.jaefan.munpyspring.animal.domain.repository.ProtectionAnimalRepository;
import com.jaefan.munpyspring.animal.presentation.dto.AnimalSearchRequestDto;
import com.jaefan.munpyspring.animal.presentation.dto.AnimalSearchResponseDto;
import com.jaefan.munpyspring.common.util.PageableConst;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;
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

		List<Long> shelterIds = shelterRepository.findByRegion(upper, List.of(lower)).stream()
			.map(Shelter::getId)
			.toList();

		AnimalSearchCondition animalSearchCondition = AnimalSearchCondition.builder()
			.shelterIds(shelterIds)
			.animalType(animalSearchRequestDto.getAnimalType())
			.breedType(animalSearchRequestDto.getBreedType())
			.gender(animalSearchRequestDto.getGender())
			.build();

		Integer size = animalSearchRequestDto.getSize();
		Integer page = animalSearchRequestDto.getPage();
		if (page != null) {
			if (size == null) {
				size = PageableConst.DEFAULT_SIZE;
			}

			Pageable pageable = PageRequest.of(page - 1, size);

			return protectionAnimalRepository.findProtectionAnimalsWithPagination(animalSearchCondition, pageable).stream()
				.map(ProtectionAnimal::toResponseDto)
				.toList();
		}

		return protectionAnimalRepository.findProtectionAnimals(animalSearchCondition).stream()
			.map(ProtectionAnimal::toResponseDto)
			.toList();
	}
}

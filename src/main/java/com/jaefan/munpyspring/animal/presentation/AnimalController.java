package com.jaefan.munpyspring.animal.presentation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.animal.application.AnimalSearchService;
import com.jaefan.munpyspring.animal.domain.model.AnimalType;
import com.jaefan.munpyspring.animal.presentation.dto.AnimalSearchRequestDto;
import com.jaefan.munpyspring.animal.presentation.dto.AnimalSearchResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/animals")
@RequiredArgsConstructor
public class AnimalController {

	private final AnimalSearchService animalSearchService;

	@GetMapping
	public ResponseEntity<List<AnimalSearchResponseDto>> searchAnimals(@ModelAttribute AnimalSearchRequestDto animalSearchRequestDto) {
		List<AnimalSearchResponseDto> searchResults = animalSearchService.findAnimals(animalSearchRequestDto);

		return ResponseEntity.ok(searchResults);
	}

	@GetMapping("/breeds")
	public ResponseEntity<List<String>> getBreeds(AnimalType animalType) {
		List<String> breedNames = animalSearchService.findBreedNames(animalType);

		return ResponseEntity.ok(breedNames);
	}
}

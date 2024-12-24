package com.jaefan.munpyspring.animal.presentation;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.animal.application.AnimalRegistrationService;
import com.jaefan.munpyspring.animal.presentation.dto.AnimalRegistrationDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AnimalRegistrationController {
	private final AnimalRegistrationService animalRegistrationService;

	@PostMapping("/api/animal")
	public ResponseEntity<String> registerAnimal(
		@Valid @ModelAttribute AnimalRegistrationDto animalRegistrationDto) throws
		IOException {
		animalRegistrationService.regist(animalRegistrationDto);
		return new ResponseEntity<>("Registration Success", HttpStatus.CREATED);
	}
}

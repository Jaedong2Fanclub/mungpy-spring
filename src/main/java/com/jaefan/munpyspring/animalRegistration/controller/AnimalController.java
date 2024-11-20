package com.jaefan.munpyspring.animalRegistration.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.animalRegistration.dto.AnimalRegistRequestDto;
import com.jaefan.munpyspring.animalRegistration.service.AnimalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AnimalController {
	private final AnimalService animalService;

	@PostMapping("/api/animal")
	public ResponseEntity<String> registerAnimal(
		@Valid @ModelAttribute AnimalRegistRequestDto animalRegistRequestDto) throws
		IOException {
		animalService.regist(animalRegistRequestDto);
		return new ResponseEntity<>("Registration Success", HttpStatus.CREATED);
	}
}

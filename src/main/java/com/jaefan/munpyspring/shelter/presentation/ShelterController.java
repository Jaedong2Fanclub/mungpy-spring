package com.jaefan.munpyspring.shelter.presentation;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.shelter.application.ShelterService;
import com.jaefan.munpyspring.shelter.presentation.dto.ShelterSignUpRequestDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShelterController {

	private final ShelterService shelterService;

	@PostMapping("/shelter")
	public ResponseEntity<String> signUpShelter(@Valid @ModelAttribute ShelterSignUpRequestDto shelterSignUpRequestDto) throws IOException {
		shelterService.signUpShelter(shelterSignUpRequestDto);
		return new ResponseEntity<>("signUp Success", CREATED);
	}
}

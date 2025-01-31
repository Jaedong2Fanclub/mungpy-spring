package com.jaefan.munpyspring.shelter.presentation;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.shelter.application.ShelterService;
import com.jaefan.munpyspring.shelter.presentation.dto.ShelterResponseDto;
import com.jaefan.munpyspring.shelter.presentation.dto.ShelterSearchDto;
import com.jaefan.munpyspring.shelter.presentation.dto.ShelterSignUpRequestDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
public class ShelterController {

	private final ShelterService shelterService;

	@GetMapping
	public ResponseEntity<List<ShelterResponseDto>> getShelters(@ModelAttribute ShelterSearchDto shelterSearchDto) {
		List<ShelterResponseDto> shelters = shelterService.findByRegion(shelterSearchDto.getUpper(), shelterSearchDto.getLower());

		return ResponseEntity.ok(shelters);
	}

	@PostMapping
	public ResponseEntity<String> signUpShelter(@Valid @ModelAttribute ShelterSignUpRequestDto shelterSignUpRequestDto) throws IOException {
		shelterService.signUpShelter(shelterSignUpRequestDto);
		return new ResponseEntity<>("signUp Success", CREATED);
	}
}

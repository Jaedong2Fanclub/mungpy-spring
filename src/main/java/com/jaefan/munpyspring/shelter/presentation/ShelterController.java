package com.jaefan.munpyspring.shelter.presentation;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.shelter.application.ShelterInitializeService;
import com.jaefan.munpyspring.shelter.application.ShelterService;
import com.jaefan.munpyspring.shelter.presentation.dto.ShelterResponseDto;
import com.jaefan.munpyspring.shelter.presentation.dto.ShelterSignUpRequestDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
public class ShelterController {

	private final ShelterService shelterService;
	private final ShelterInitializeService shelterInitializeService;

	@GetMapping
	public ResponseEntity<List<ShelterResponseDto>> getShelters(@RequestParam Map<String, String> regionMap) {
		List<ShelterResponseDto> shelters = shelterService.findByRegion(regionMap);

		return ResponseEntity.ok(shelters);
	}

	@PostMapping
	public ResponseEntity<String> signUpShelter(@Valid @ModelAttribute ShelterSignUpRequestDto shelterSignUpRequestDto) throws IOException {
		shelterService.signUpShelter(shelterSignUpRequestDto);
		return new ResponseEntity<>("signUp Success", CREATED);
	}

	@GetMapping("/init")
	public ResponseEntity<String> initShelter() {
		shelterInitializeService.init();

		return ResponseEntity.ok("Shelter successfully initialized");
	}
}

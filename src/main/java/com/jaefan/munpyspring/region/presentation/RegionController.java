package com.jaefan.munpyspring.region.presentation;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.region.application.RegionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/regions")
@RequiredArgsConstructor
public class RegionController {

	private final RegionService regionService;

	@GetMapping
	public ResponseEntity<Map<String, List<String>>> getAllRegions() {
		return ResponseEntity.ok(regionService.getAllRegions());
	}

	@GetMapping("/init")
	public ResponseEntity<Void> initRegions() throws IOException {
		regionService.initRegions();

		return ResponseEntity.ok().build();
	}
}

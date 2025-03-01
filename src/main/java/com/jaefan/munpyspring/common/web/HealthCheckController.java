package com.jaefan.munpyspring.common.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health-check")
public class HealthCheckController {

	//test
	@GetMapping
	public ResponseEntity<Void> HealthCheck() {
		return ResponseEntity.ok().build();
	}
}

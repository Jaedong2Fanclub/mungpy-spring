package com.jaefan.munpyspring.animalinfo.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.animalinfo.application.CrawlingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/crawling")
@RequiredArgsConstructor
public class CrawlingController {

	private final CrawlingService crawlingService;

	@GetMapping
	public ResponseEntity<Void> crawl(String category) {
		crawlingService.crawl(category);

		return ResponseEntity.ok().build();
	}
}

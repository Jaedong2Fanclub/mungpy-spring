package com.jaefan.munpyspring.animalinfo.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
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

	@Scheduled(cron = "0 50 11,23 * * ?")
	public void crawl() {

	}

	@GetMapping
	public ResponseEntity<Void> crawlTest(String category) {
		crawlingService.crawl(category);

		return ResponseEntity.ok().build();
	}
}

package com.jaefan.munpyspring.animal.presentation;

import static com.jaefan.munpyspring.animal.domain.model.CrawlingCategory.*;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.animal.application.AnimalCrawlingService;
import com.jaefan.munpyspring.animal.domain.model.CrawlingCategory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/crawling")
@RequiredArgsConstructor
public class AnimalCrawlingController {

	private final AnimalCrawlingService crawlingService;

	@Scheduled(cron = "0 50 11,23 * * ?")
	public void crawlInsertData() {
		crawlingService.crawl(PUBLIC);
		crawlingService.crawl(PROTECTION);
	}

	@Scheduled(cron = "0 0 3 * * ?")
	public void crawlDeleteData() {
		crawlingService.crawl(RELEASE);
	}

	@GetMapping
	public ResponseEntity<Void> crawlTest(CrawlingCategory category) {
		crawlingService.crawl(category);

		return ResponseEntity.ok().build();
	}
}

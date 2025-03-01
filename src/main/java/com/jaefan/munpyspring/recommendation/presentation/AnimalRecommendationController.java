package com.jaefan.munpyspring.recommendation.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaefan.munpyspring.recommendation.application.AnimalRecommendationService;
import com.jaefan.munpyspring.recommendation.application.RequestCountService;
import com.jaefan.munpyspring.recommendation.presentation.dto.AnimalRecommendationDto;
import com.jaefan.munpyspring.recommendation.presentation.dto.RequestCountResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/matching-animals")
public class AnimalRecommendationController {

	private final AnimalRecommendationService animalRecommendationService;
	private final RequestCountService requestCountService;

	public AnimalRecommendationController(AnimalRecommendationService animalRecommendationService, RequestCountService requestCountService) {
		this.animalRecommendationService = animalRecommendationService;
		this.requestCountService = requestCountService;
	}

	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<AnimalRecommendationDto> recommendAnimal( @RequestPart("user_image") MultipartFile userImage,
																		 @RequestPart("type") String type,
																		 @RequestPart("user_traits") String userTraitsJson) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> userTraits = objectMapper.readValue(userTraitsJson, List.class);
		//TODO: 입력데이터 유효성 검사

		// 추천결과 반환
		AnimalRecommendationDto response = animalRecommendationService.getRecommendation(userImage, type, userTraits);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/request-count")
	public ResponseEntity<RequestCountResponseDto> getRequestCount() {
		int count = requestCountService.getRequestCount();
		RequestCountResponseDto response = new RequestCountResponseDto(count);
		//TODO: string으로 바꾸고 ',' 넣기 작업
		return ResponseEntity.ok(response);
	}

}








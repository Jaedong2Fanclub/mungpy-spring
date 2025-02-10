package com.jaefan.munpyspring.recommendation.application;

import com.jaefan.munpyspring.recommendation.presentation.dto.AnimalRecommendationDto;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class AnimalRecommendationService {

	private final BreedSearchService breedSearchService;

	public AnimalRecommendationService(BreedSearchService breedSearchService) {
		this.breedSearchService = breedSearchService;
	}

	public AnimalRecommendationDto getRecommendation(MultipartFile userImage, String type, List<String> userTraits) {
		AnimalRecommendationDto reslut = new AnimalRecommendationDto();
		// AI 서버에서 추천 결과 받기
		String recommendedBreed = breedSearchService.searchBreed(userImage, type);
		System.out.println("추천된 품종: " + recommendedBreed);
		//TODO: 대표이미지 삽입

		// TODO: 성향 처리

		// TODO: 카운트 업

		return AnimalRecommendationDto.builder()
			.representativeImage("https://avatars.githubusercontent.com/u/92240138?v=4")
			.breeds(recommendedBreed)
			.shortReview("사용자님은 귀여운 푸들상 입니다.")
			.detailedReview("충성심이 강하나 때때로 독립성이 있어요")
			.loyaltyScore(80)
			.healthScore(51)
			.activityScore(50)
			.intelligenceScore(80)
			.traits(List.of("산책은 필수에요", "밥은 3끼를 먹어요", "우리나라에서 가장 충심이 강해요"))
			.build();
	}
}


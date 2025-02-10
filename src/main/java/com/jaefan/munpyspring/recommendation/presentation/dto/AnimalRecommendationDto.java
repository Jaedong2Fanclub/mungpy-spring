package com.jaefan.munpyspring.recommendation.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRecommendationDto {

	private String representativeImage;
	private String breeds;
	private String shortReview;
	private String detailedReview;
	private int loyaltyScore;
	private int healthScore;
	private int activityScore;
	private int intelligenceScore;
	private List<String> traits;
}


/*

{
  "representative_image": "https://example.com/images/dog123.jpg",
  "breeds" : 귀여운 푸들
  "short_review": "사용자님은 귀여운 푸들상 입니다.",
  "detailed_review": "충성심이 강하나 때때로 독립성이 있어요. 똑똑하면서 건강하고 활동적인 성격이에요. 낯선 사람을 경계하지만 주인에 개는 일편단심이에요",
  "loyalty_score": 80,
  "health_score": 51,
  "activity_score": 50,
  "intelligence_score": 80,
  "traits": ["산책은 필수에요", "밥은 3끼를 먹어요", "우리나라에서 가장 충심이 강해요"]
}

 */


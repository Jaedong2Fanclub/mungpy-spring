package com.jaefan.munpyspring.shelter.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.jaefan.munpyspring.shelter.presentation.dto.Coordinate;
import com.jaefan.munpyspring.shelter.presentation.feignClient.KakaoMapFeignClient;

import lombok.RequiredArgsConstructor;

/**
 * kakaoMapFeignClient를 이용해 문자열 주소에 대한 경,위도 값을 받아오는 모듈
 */
@Component
@RequiredArgsConstructor
public class KakaoMapHttpClient {
	@Value("${oauth2.kakao.api.client-id}")
	private String clientId;

	private final KakaoMapFeignClient kakaoMapFeignClient;

	public Coordinate getCoordinateByAddress(String address) {
		JsonNode response = kakaoMapFeignClient.getCoordinatesByAddress("KakaoAK " + clientId, address, 1, 1);

		Double latitude = null;
		Double longitude = null;

		int totalCount = response.path("meta").path("total_count").asInt();

		if (totalCount != 0) { // 조회 결과가 존재하면 위도,경도 파싱 수행 (결과가 없을 경우 경,위도는 null)
			latitude = response.path("documents").get(0).path("x").asDouble();
			longitude = response.path("documents").get(0).path("y").asDouble();
		}

		return new Coordinate(latitude, longitude);
	}
}

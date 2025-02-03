package com.jaefan.munpyspring.shelter.presentation.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 카카오 맵 서버로 API 요청을 보내는 객체
 */
@FeignClient(name = "KakaoMapFeignClient", url = "https://dapi.kakao.com")
public interface KakaoMapFeignClient {
	@GetMapping(value = "/v2/local/search/address.json")
	JsonNode getCoordinatesByAddress(
		@RequestHeader("Authorization") String clientId, // Kakao API KEY
		@RequestParam("query") String query, // 주소명
		@RequestParam("page") Integer page, // 반환 받을 페이지 수
		@RequestParam("size") Integer size // 페이지 당 데이터 수
	);
}

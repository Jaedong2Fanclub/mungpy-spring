package com.jaefan.munpyspring.recommendation.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
	import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

@Service
public class BreedSearchService {

	@Value("${recommendation.server-url}")
	private String AiServerUrl;
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public BreedSearchService(RestTemplateBuilder builder, ObjectMapper objectMapper) {
		this.restTemplate = builder.build();
		this.objectMapper = objectMapper;
	}

	public String searchBreed(MultipartFile userImage, String type) {
		try {
			//url 설정
			String apiUrl = String.format("%s?type=%s", AiServerUrl + "/breeds", type);

			// 헤더 설정
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);

			// Multipart 요청 생성
			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.add("file", userImage.getResource());

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
			// API 호출
			ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
			// 응답 JSON 파싱
			Map<String, String> responseBody = objectMapper.readValue(response.getBody(), Map.class);
			return responseBody.getOrDefault("result", "unknown");

		} catch (IOException e) {
			throw new RuntimeException("Breed search failed", e);
		}
	}
}


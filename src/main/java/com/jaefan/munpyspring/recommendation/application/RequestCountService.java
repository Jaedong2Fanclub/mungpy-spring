package com.jaefan.munpyspring.recommendation.application;

import com.jaefan.munpyspring.recommendation.domain.repository.RequestCountRepository;
import com.jaefan.munpyspring.recommendation.domain.model.RequestCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class RequestCountService {

	private final RequestCountRepository requestCountRepository;

	@Autowired
	public RequestCountService(RequestCountRepository requestCountRepository) {
		this.requestCountRepository = requestCountRepository;
	}

	// 요청 카운트 증가
	@Transactional
	public void incrementRequestCount() {
		RequestCount requestCount = requestCountRepository.findByIdForUpdate(1L);
		if (requestCount == null) {
			requestCount = new RequestCount(0); // 최초 요청시엔 count 0으로 초기화
		}
		requestCount.setCount(requestCount.getCount() + 1);
		requestCountRepository.save(requestCount);
	}

	// 현재 요청 카운트 반환
	@Transactional
	public int getRequestCount() {
		RequestCount requestCount = requestCountRepository.findByIdForUpdate(1L);
		if (requestCount == null) {
			requestCount = new RequestCount(0); // 최초 요청시엔 count 0으로 초기화
		}
		return requestCount.getCount();
	}
}

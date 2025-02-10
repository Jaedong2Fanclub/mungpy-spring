package com.jaefan.munpyspring.recommendation.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class RequestCount {

	@Id
	private Long id = 1L;  // 요청 카운트를 1개의 레코드로 관리 (단일 레코드)
	private int count;

	// 기본 생성자
	public RequestCount() {}

	// 생성자
	public RequestCount(int count) {
		this.count = count;
	}

	// Getter & Setter
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}

package com.jaefan.munpyspring.recommendation.presentation.dto;

public class RequestCountResponseDto {

	private int count;

	public RequestCountResponseDto(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}

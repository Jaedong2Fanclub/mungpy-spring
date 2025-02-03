package com.jaefan.munpyspring.shelter.presentation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 주소에 대한 경,위도 객체
 */
@Getter
@RequiredArgsConstructor
public class Coordinate {
	private final Double latitude;
	private final Double longitude;
}

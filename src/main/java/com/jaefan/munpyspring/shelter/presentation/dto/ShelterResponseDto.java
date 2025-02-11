package com.jaefan.munpyspring.shelter.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ShelterResponseDto {

	private String name;
	private String address;
	private String telno;
	private Double longitude; // 경도
	private Double latitude; // 위도
}

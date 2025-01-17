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
	private String owner;
	private String telno;
	private String longitude; // 경도
	private String latitude; // 위도
}

package com.jaefan.munpyspring.shelter.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ShelterSearchDto {
	private String upper;
	private String lower;
}

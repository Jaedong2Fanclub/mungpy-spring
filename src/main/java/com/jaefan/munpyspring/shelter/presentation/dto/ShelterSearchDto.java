package com.jaefan.munpyspring.shelter.presentation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ShelterSearchDto {
	private List<String> uppers;
	private List<String> lowers;
}

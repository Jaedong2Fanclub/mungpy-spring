package com.jaefan.munpyspring.animal.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jaefan.munpyspring.animal.domain.model.AnimalGender;
import com.jaefan.munpyspring.animal.domain.model.AnimalNeutered;
import com.jaefan.munpyspring.animal.domain.model.AnimalType;
import com.jaefan.munpyspring.animal.domain.model.Breed;
import com.jaefan.munpyspring.animal.domain.model.PublicAnimal;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnimalRegistrationDto {

	@NotBlank
	@Pattern(regexp = "DOG|CAT|OTHER", message = "축종은 DOG, CAT, OTHER 중 하나여야 합니다.")
	private AnimalType type;

	@NotBlank
	@Pattern(regexp = "MALE|FEMALE|UNKNOWN", message = "성별은 MALE, FEMALE, UNKNOWN 중 하나여야 합니다.")
	private AnimalGender gender;

	@NotBlank
	@Pattern(regexp = "YES|NO|UNKNOWN", message = "중성화 여부는 YES, NO, UNKNOWN 중 하나여야 합니다.")
	private AnimalNeutered isNeutered;

	@NotNull
	private Boolean caution;

	@NotBlank
	@Size(min = 1, max = 50, message = "공고번호는 1 ~ 50자 사이여야 합니다.")
	private String noticeNo;

	@NotNull
	private List<MultipartFile> animalImages;

	@NotNull
	private LocalDateTime rescuedAt;

	@NotBlank
	@Size(min = 1, max = 150, message = "구조 장소는 1~150자 사이여야 합니다.")
	private String rescuePlace;

	@Size(min = 1, max = 300, message = "구조 사유는 1~300자 사이여야 합니다.")
	private String rescueReason;

	public static PublicAnimal toEntity(AnimalRegistrationDto animalRegistrationDTO, Shelter shelter, Breed breed) {
		return new PublicAnimal(
			animalRegistrationDTO.getType(),
			animalRegistrationDTO.getGender(),
			animalRegistrationDTO.getIsNeutered(),
			animalRegistrationDTO.getCaution(),
			animalRegistrationDTO.getNoticeNo(),
			animalRegistrationDTO.getRescuedAt(),
			animalRegistrationDTO.getRescuePlace(),
			animalRegistrationDTO.getRescueReason(),
			shelter,
			breed
		);
	}
}

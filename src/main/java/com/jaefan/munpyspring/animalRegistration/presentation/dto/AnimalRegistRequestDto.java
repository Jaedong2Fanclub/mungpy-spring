package com.jaefan.munpyspring.animalRegistration.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.jaefan.munpyspring.animalRegistration.domain.model.Animal;
import com.jaefan.munpyspring.animalRegistration.domain.model.Breed;
import com.jaefan.munpyspring.animalRegistration.domain.model.ProtectionStatus;
import com.jaefan.munpyspring.signUp.domain.model.Shelter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnimalRegistRequestDto {

	@NotBlank
	@Size(min = 3, max = 3, message = "동물과는 DOG/CAT와 같이 반드시 3자여야 합니다.")
	private String type;

	@NotBlank
	@Size(min = 1, max = 7, message = "성별은 1 ~ 7자 사이여야 합니다.")
	private String gender;

	@NotBlank
	@Size(min = 1, max = 7, message = "중성화는 1 ~ 7자 사이여야 합니다.")
	private String isNeutered;

	@NotNull
	private Boolean caution;

	@NotBlank
	@Size(min = 1, max = 50, message = "공고번호는 1 ~ 50자 사이여야 합니다.")
	private String noticeNo;

	@NotNull
	private ProtectionStatus protectionStatus; // TODO 이거 protectionStatus로 바꿔야 함.

	private LocalDateTime exitAt;

	@NotNull
	private List<MultipartFile> animalImages;

	@NotNull
	private LocalDateTime rescuedAt;

	@NotBlank
	@Size(min = 1, max = 150, message = "구조 장소는 1~150자 사이여야 합니다.")
	private String rescuePlace;

	@Size(min = 1, max = 300, message = "구조 사유는 1~300자 사이여야 합니다.")
	private String rescueReason;

	@NotNull
	private LocalDateTime dueAt;

	public static Animal toEntity(AnimalRegistRequestDto animalRegistRequestDTO, Shelter shelter, Breed breed) {
		return new Animal(
			animalRegistRequestDTO.getType(),
			animalRegistRequestDTO.getGender(),
			animalRegistRequestDTO.getIsNeutered(),
			animalRegistRequestDTO.getCaution(),
			animalRegistRequestDTO.getNoticeNo(),
			animalRegistRequestDTO.getProtectionStatus(),
			animalRegistRequestDTO.getExitAt(),
			animalRegistRequestDTO.getRescuedAt(),
			animalRegistRequestDTO.getRescuePlace(),
			animalRegistRequestDTO.getRescueReason(),
			animalRegistRequestDTO.getDueAt(),
			shelter,
			breed
		);
	}
}

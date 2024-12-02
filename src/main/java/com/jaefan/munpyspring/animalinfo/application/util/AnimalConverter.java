package com.jaefan.munpyspring.animalinfo.application.util;

import static com.jaefan.munpyspring.animalinfo.domain.model.AnimalType.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jaefan.munpyspring.animalinfo.domain.model.AnimalGender;
import com.jaefan.munpyspring.animalinfo.domain.model.AnimalNeutered;
import com.jaefan.munpyspring.animalinfo.domain.model.AnimalType;
import com.jaefan.munpyspring.animalinfo.domain.model.ProtectionStatus;
import com.jaefan.munpyspring.animalinfo.domain.model.PublicAnimal;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.jaefan.munpyspring.shelter.domain.repository.ShelterRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnimalConverter {

	private final ShelterRepository shelterRepository;

	public PublicAnimal convertMapToPublic(Map<String, String> map) {
		AnimalType type = switch (map.get("동물종류")) {
			case "개" -> DOG;
			case "고양이" -> CAT;
			default -> OTHER;
		};

		if (type.equals(OTHER)) {
			return null;
		}

		AnimalGender gender = getAnimalGender(map);

		AnimalNeutered isNeutered = getIsNeutered(map);

		boolean caution = hasRiskKeyword(map.get("특징"));

		String dateString = map.get("공고기간").split("~")[1].trim();
		LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
		LocalDateTime dueAt = date.atTime(23, 59, 59);

		String shelterName = map.get("관할 보호센터명").trim();
		String telno = map.get("전화번호").trim();

		Shelter newShelter = Shelter.builder()
			.name(shelterName)
			.owner(map.get("대표자").trim())
			.address(map.get("주소").trim())
			.telno(telno)
			.build();

		Shelter shelter = shelterRepository.findByNameAndTelno(shelterName, telno).orElse(newShelter);

		return PublicAnimal.builder()
			.type(type)
			.gender(gender)
			.isNeutered(isNeutered)
			.caution(caution)
			.noticeNo(map.get("공고번호").trim())
			.protectionStatus(ProtectionStatus.ENTER)
			.rescuedAt(LocalDateTime.now())
			.rescuePlace(map.get("구조장소").trim())
			.rescueReason(map.get("구조사유").trim())
			.dueAt(dueAt)
			.shelter(shelter)
			.build();
	}

	private AnimalNeutered getIsNeutered(Map<String, String> map) {
		return switch (map.get("중성화 여부")) {
			case "예" -> AnimalNeutered.YES;
			case "아니오" -> AnimalNeutered.NO;
			default -> AnimalNeutered.UNKNOWN;
		};
	}

	private AnimalGender getAnimalGender(Map<String, String> map) {
		return switch (map.get("성별")) {
			case "수컷" -> AnimalGender.MALE;
			case "암컷" -> AnimalGender.FEMALE;
			default -> AnimalGender.UNKNOWN;
		};
	}

	private boolean hasRiskKeyword(String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}

		String[] keywords = {"상처", "부상", "질환", "골절", "마비", "실명"};

		for (String keyword : keywords) {
			if (str.contains(keyword)) {
				return true;
			}
		}

		return false;
	}
}

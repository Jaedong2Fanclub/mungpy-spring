package com.jaefan.munpyspring.animal.application.util;

import static com.jaefan.munpyspring.animal.domain.model.AnimalType.*;
import static com.jaefan.munpyspring.animal.domain.model.ProtectionStatus.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.jaefan.munpyspring.animal.domain.model.AnimalGender;
import com.jaefan.munpyspring.animal.domain.model.AnimalNeutered;
import com.jaefan.munpyspring.animal.domain.model.AnimalType;
import com.jaefan.munpyspring.animal.domain.model.ProtectionAnimal;
import com.jaefan.munpyspring.animal.domain.model.PublicAnimal;
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

		AnimalNeutered isNeutered = getIsNeutered(map, "중성화 여부");

		boolean caution = hasRiskKeyword(map.get("특징"));

		String rescueDateString = map.get("구조일").trim();
		LocalDate rescueDate = LocalDate.parse(rescueDateString, DateTimeFormatter.ISO_DATE);
		LocalDateTime rescuedAt = rescueDate.atStartOfDay();

		String endDateString = map.get("공고기간").split("~")[1].trim();
		LocalDate endDate = LocalDate.parse(endDateString, DateTimeFormatter.ISO_DATE);
		LocalDateTime dueAt = endDate.atTime(23, 59, 59);

		String shelterName = map.get("관할 보호센터명").trim();
		String telno = map.get("전화번호").trim();

		Shelter newShelter = Shelter.builder()
			.name(shelterName)
			.owner(map.get("대표자").trim())
			.address(map.get("주소").trim())
			.telNo(telno)
			.build();

		Shelter shelter = shelterRepository.findByNameAndTelNo(shelterName, telno).orElse(newShelter);

		return PublicAnimal.builder()
			.type(type)
			.gender(gender)
			.isNeutered(isNeutered)
			.caution(caution)
			.noticeNo(map.get("공고번호").trim())
			.protectionStatus(POSTED)
			.rescuedAt(rescuedAt)
			.rescuePlace(map.get("구조장소").trim())
			.rescueReason(map.get("구조사유").trim())
			.dueAt(dueAt)
			.shelter(shelter)
			.build();
	}

	public ProtectionAnimal convertMapToProtection(Map<String, String> map) {
		String typeString = map.get("품종");
		AnimalType type = null;
		if (typeString.contains("[개]")) {
			type = DOG;
		} else if (typeString.contains("[고양이]")) {
			type = CAT;
		}

		if (type == null) {
			return null;
		}

		AnimalGender gender = getAnimalGender(map);

		AnimalNeutered isNeutered = getIsNeutered(map, "중성화");

		boolean caution = hasRiskKeyword(map.get("특징(건강)"));

		String[] physicalInfos = map.get("나이/체중").split("/");
		String age = physicalInfos[0].trim();
		double weight = Double.parseDouble(physicalInfos[1].replace("(Kg)", "").trim());

		String shelterName = map.get("보호센터").trim();
		String telNo = map.get("보호센터연락처").trim();

		Shelter newShelter = Shelter.builder()
			.name(shelterName)
			.owner(map.get("관할기관").trim())
			.address(map.get("보호장소").trim())
			.telNo(telNo)
			.build();

		Shelter shelter = shelterRepository.findByNameAndTelNo(shelterName, telNo).orElse(newShelter);

		return ProtectionAnimal.builder()
			.type(type)
			.gender(gender)
			.isNeutered(isNeutered)
			.caution(caution)
			.noticeNo(map.get("공고번호").trim())
			.age(age)
			.weight(weight)
			.protectionStatus(PROTECTED)
			.announcedAt(LocalDateTime.now())
			.rescuePlace(map.get("발생장소").trim())
			.rescueDetail(map.get("구조시 특징").trim())
			.medicalCheck(map.get("건강검진"))
			.vaccination(map.get("접종상태"))
			.shelter(shelter)
			.build();
	}

	private AnimalNeutered getIsNeutered(Map<String, String> map, String key) {
		return switch (map.get(key)) {
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

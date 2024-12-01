package com.jaefan.munpyspring.animalRegistration.application;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jaefan.munpyspring.animalRegistration.domain.model.Animal;
import com.jaefan.munpyspring.animalRegistration.domain.model.AnimalImage;
import com.jaefan.munpyspring.animalRegistration.domain.model.Breed;
import com.jaefan.munpyspring.animalRegistration.domain.repository.AnimalRepository;
import com.jaefan.munpyspring.animalRegistration.presentation.dto.AnimalRegistRequestDto;
import com.jaefan.munpyspring.common.util.S3Uploader;
import com.jaefan.munpyspring.signUp.domain.model.Shelter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalService {
	private final AnimalRepository animalRepository;
	private final S3Uploader s3Uploader;

	@Transactional
	public void regist(AnimalRegistRequestDto animalRegistRequestDto) throws IOException {
		String animalType = animalRegistRequestDto.getType(); // S3 업로드 시 개/고양이 디렉토리 구분하여 저장하기 때문에 과 추출
		List<MultipartFile> animalRegistrationImages = animalRegistRequestDto.getAnimalImages();

		List<String> s3Urls = s3Uploader.upload(animalRegistrationImages, animalType);

		Shelter shelter = null; // Spring Context에서 Authentication 객체를 꺼내 현재 접속 보호소를 식별하는 코드 들어갈 자리
		Breed breed = null; // AI 서버에 동물 이미지 또는 S3url 전송 후 판정 품종 받아오는 코드 들어갈 자리 (새로운 품종 시 품종 신규 등록 필요)

		Animal animal = AnimalRegistRequestDto.toEntity(animalRegistRequestDto, shelter, breed);
		List<AnimalImage> animalImages = s3Urls.stream()
			.map(s3Url -> new AnimalImage(s3Url, animal))
			.toList(); // S3에서 반환받은 Url 리스트로 AnimalImage 엔티티 생성
		animal.setAnimalImages(
			animalImages); // setter로 동물 엔티티에 이미지 추가. (Cascade.ALL로 인해 Animal 저장 시 AnimalImage도 일괄 저장됨)
		animalRepository.save(animal);
	}
}

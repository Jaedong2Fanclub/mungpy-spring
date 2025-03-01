package com.jaefan.munpyspring.shelter.application;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaefan.munpyspring.common.exception.DuplicateEntityException;
import com.jaefan.munpyspring.common.util.GoogleCloudStroageUploader;
import com.jaefan.munpyspring.common.util.PageableConst;
import com.jaefan.munpyspring.region.domain.model.Region;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.jaefan.munpyspring.shelter.domain.repository.ShelterRepository;
import com.jaefan.munpyspring.shelter.presentation.dto.Coordinate;
import com.jaefan.munpyspring.shelter.presentation.dto.ShelterResponseDto;
import com.jaefan.munpyspring.shelter.presentation.dto.ShelterSignUpRequestDto;
import com.jaefan.munpyspring.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShelterService {

	@Value("${oauth2.kakao.api.client-id}")
	private String clientId;
	private final ShelterRepository shelterRepository;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final GoogleCloudStroageUploader googleCloudStroageUploader;

	private final AddressToRegionConverter addressToRegionConverter; // 문자열 주소를 Region 엔티티로 라벨링하는 모듈

	private final KakaoMapHttpClient kakaoMapHttpClient; // 문자열 주소에 대한 경,위도 좌표를 반환하는 카카오 Map API 클라이언트

	private final static String DIR_NAME = "SHELTER";

	public List<ShelterResponseDto> findByRegion(Map<String, String> regionMap, Integer size, Integer page) {
		List<Shelter> shelters;
		if (page != null) {
			if (size == null) {
				size = PageableConst.DEFAULT_SIZE;
			}

			if (page <= 0) {
				throw new IllegalArgumentException("Page must be greater than 0");
			}
			if (size <= 0) {
				throw new IllegalArgumentException("Size must be greater than 0");
			}

			shelters = shelterRepository.findByRegionWithPagination(regionMap, PageRequest.of(page - 1, size));
		} else {
			shelters = shelterRepository.findByRegion(regionMap);
		}

		return shelters.stream()
			.map(this::createResposneDto)
			.toList();
	}

	private ShelterResponseDto createResposneDto(Shelter shelter) {
		return ShelterResponseDto.builder()
			.name(shelter.getName())
			.address(shelter.getAddress())
			.telno(shelter.getTelNo())
			.longitude(shelter.getLongitude())
			.latitude(shelter.getLatitude())
			.build();
	}

	@Transactional
	public void signUpShelter(ShelterSignUpRequestDto shelterSignUpRequestDto) throws IOException {
		if (userRepository.existsByEmail(shelterSignUpRequestDto.getEmail())) {
			throw new DuplicateEntityException("이미 존재하는 이메일입니다.");
		}

		if (userRepository.existsByNickname(shelterSignUpRequestDto.getNickname())) {
			throw new DuplicateEntityException("이미 존재하는 닉네임입니다.");
		}

		String imageUrl = null;

		if (shelterSignUpRequestDto.getProfileImage() != null) {
			imageUrl = googleCloudStroageUploader.upload(shelterSignUpRequestDto.getProfileImage(), DIR_NAME);
		}

		Region region = addressToRegionConverter.getRegionFromAddress(shelterSignUpRequestDto.getAddress());

		Coordinate coordinate = kakaoMapHttpClient.getCoordinateByAddress(shelterSignUpRequestDto.getAddress());

		Shelter shelter = shelterSignUpRequestDto.toShelter(imageUrl, region, coordinate.getLatitude(), coordinate.getLongitude());

		shelter.hashPassword(passwordEncoder);

		shelterRepository.save(shelter);
	}
}

package com.jaefan.munpyspring.shelter.application;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaefan.munpyspring.common.exception.DuplicateEntityException;
import com.jaefan.munpyspring.common.util.GoogleCloudStroageUploader;
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

	public List<ShelterResponseDto> findByRegion(String upper, String lower) {
		return shelterRepository.findByRegion(upper, lower).stream()
			.map(shelter -> {
				// TODO : 주소로 좌표 받아오기 (Open Feign 써서)
				String longitude = "";
				String latitude = "";

				return ShelterResponseDto.builder()
					.name(shelter.getName())
					.address(shelter.getAddress())
					.owner(shelter.getOwner())
					.telno(shelter.getTelNo())
					.longitude(longitude)
					.latitude(latitude)
					.build();
			})
			.toList();
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

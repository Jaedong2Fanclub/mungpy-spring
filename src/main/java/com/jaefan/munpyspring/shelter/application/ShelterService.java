package com.jaefan.munpyspring.shelter.application;

import java.io.IOException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaefan.munpyspring.common.exception.DuplicateEntityException;
import com.jaefan.munpyspring.common.util.GoogleCloudStroageUploader;
import com.jaefan.munpyspring.shelter.domain.model.Shelter;
import com.jaefan.munpyspring.shelter.domain.repository.ShelterRepository;
import com.jaefan.munpyspring.shelter.presentation.dto.ShelterSignUpRequestDto;
import com.jaefan.munpyspring.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShelterService {

	private final ShelterRepository shelterRepository;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final GoogleCloudStroageUploader googleCloudStroageUploader;

	private final static String DIR_NAME = "SHELTER";

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

		Shelter shelter = ShelterSignUpRequestDto.toShelter(shelterSignUpRequestDto, imageUrl);

		shelter.hashPassword(passwordEncoder);

		shelterRepository.save(shelter);
	}
}

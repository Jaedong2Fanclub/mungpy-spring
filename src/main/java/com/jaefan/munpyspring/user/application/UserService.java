package com.jaefan.munpyspring.user.application;

import static com.jaefan.munpyspring.user.domain.model.Provider.*;
import static com.jaefan.munpyspring.user.domain.model.Role.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaefan.munpyspring.common.exception.DuplicateEntityException;
import com.jaefan.munpyspring.common.util.GoogleCloudStroageUploader;
import com.jaefan.munpyspring.security.domain.model.JwtTokens;
import com.jaefan.munpyspring.security.provider.JwtProvider;
import com.jaefan.munpyspring.user.domain.model.Provider;
import com.jaefan.munpyspring.user.domain.model.User;
import com.jaefan.munpyspring.user.domain.repository.UserRepository;
import com.jaefan.munpyspring.user.presentation.dto.OAuthAccountDto;
import com.jaefan.munpyspring.user.presentation.dto.UserSignUpRequestDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final RandomNicknameService randomNicknameService;

	private final JwtProvider jwtProvider;

	private final UserRepository userRepository;

	private final GoogleCloudStroageUploader googleCloudStroageUploader;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private final static String DIR_NAME = "USER";

	@Transactional
	public JwtTokens signUpOrLogin(OAuthAccountDto oAuthAccountDto, Provider provider) {

		// email로 유저를 찾고 없으면 User를 생성
		User user = userRepository.findByEmail(oAuthAccountDto.getEmail())
			.orElseGet(() -> OAuthAccountDto.toUser(oAuthAccountDto, randomNicknameService.generate(), provider, USER));

		if (user.getProviderType() != provider) {
			throw new DuplicateEntityException("이미 존재하는 이메일입니다.");
		}

		user.updateLastVisit();

		user.updateProfileImage(oAuthAccountDto.getProfileImage()); // User의 프로필 url을 카카오 리소스 서버에서 가져온 url로 최신화

		userRepository.save(user); // 요청자가 signUp인지 Login인지 구분없이 일괄 save 처리

		return jwtProvider.createJwtTokensWithEmail(user.getEmail());
	}

	@Transactional
	public JwtTokens signUp(UserSignUpRequestDto userSignUpRequestDto) throws IOException {

		if (userRepository.existsByEmail(userSignUpRequestDto.getEmail())) {
			throw new DuplicateEntityException("이미 존재하는 이메일입니다.");
		}

		if (userRepository.existsByNickname(userSignUpRequestDto.getNickname())) {
			throw new DuplicateEntityException("이미 존재하는 닉네임입니다.");
		}

		String imageUrl = googleCloudStroageUploader.upload(userSignUpRequestDto.getProfileImage(), DIR_NAME);

		User user = userSignUpRequestDto.toUser(imageUrl, DEFAULT, USER);

		user.hashPassword(passwordEncoder);

		userRepository.save(user);

		return jwtProvider.createJwtTokensWithEmail(user.getEmail());
	}

	@Transactional
	public void updateVisitAt(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
		user.updateLastVisit();
	}

	public void find(UUID uuid) {
		Optional<User> optionalUser = userRepository.findByUuid(uuid);
	}

	public String refreshAccessToken(String refreshToken) {
		return jwtProvider.refreshAccessToken(refreshToken);
	}
}

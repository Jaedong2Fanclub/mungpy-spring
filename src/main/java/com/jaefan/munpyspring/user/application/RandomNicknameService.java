package com.jaefan.munpyspring.user.application;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jaefan.munpyspring.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * OAuth 회원 랜덤 닉네임 생성기
 */
@Service
@RequiredArgsConstructor
public class RandomNicknameService {

	private final UserRepository userRepository;

	private final Random random = new Random();

	private final String[] characters = {
		"알알한", "앙칼진", "표독한", "귀여운", "순수한", "산만한", "똑똑한", "활발한", "조용한", "멋있는",
		"아련한", "화난", "행복한", "온순한", "소심한", "느긋한", "차분한", "대담한", "외향적", "내향적",
		"기쁜", "삐진"
	};

	private final String[] breeds = {
		"치와와", "불독", "말티즈", "말티푸", "핀셔", "푸들", "시츄", "버먼", "비글", "하바니즈",
		"퍼그", "테리어", "요크셔", "사모예드", "벵갈", "사바나", "랙돌", "봄베이", "메인쿤", "페르시안",
		"스핑크스", "멍뭉이", "고양이", "강아지", "냐옹이"
	};

	public String generate() {
		for (int i = 0; i < 3; i++) { // 랜덤 닉네임 생성 시도를 최대 3번까지 수행
			String character = characters[random.nextInt(characters.length)];
			String breed = breeds[random.nextInt(breeds.length)];
			String numbers = "";
			for (int j = 0; j < 4; j++) {
				numbers += random.nextInt(10);
			}
			String nickname = character + breed + "_" + numbers;
			if (!userRepository.existsByNickname(nickname)) { // 없는 닉네임인 경우 닉네임 리턴
				return nickname;
			}
		}
		return UUID.randomUUID().toString(); // 유니크한 랜덤 닉네임 생성 실패 시 uuid를 String으로 반환
	}
}

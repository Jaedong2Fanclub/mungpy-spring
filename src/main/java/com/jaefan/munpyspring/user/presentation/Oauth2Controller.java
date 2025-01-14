package com.jaefan.munpyspring.user.presentation;

import static com.jaefan.munpyspring.user.domain.model.Provider.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.common.util.CookieUtil;
import com.jaefan.munpyspring.security.domain.model.JwtTokens;
import com.jaefan.munpyspring.user.application.KakaoOAuthProvider;
import com.jaefan.munpyspring.user.application.NaverOAuthProvider;
import com.jaefan.munpyspring.user.application.UserService;
import com.jaefan.munpyspring.user.presentation.dto.OAuthAccountDto;
import com.jaefan.munpyspring.user.presentation.dto.OAuthTokenDto;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class Oauth2Controller {

	private final KakaoOAuthProvider kakaoOAuthProvider;

	private final NaverOAuthProvider naverOAuthProvider;

	private final UserService userService;

	private final CookieUtil cookieUtil;

	@GetMapping("/auth/kakao/callback")
	public ResponseEntity<String> kakaoCallback(@RequestParam String code, HttpServletResponse response) {
		OAuthTokenDto tokenByCode = kakaoOAuthProvider.getTokenByCode(code); // 카카오 계정 정보 요청을 위한 인증 토큰
		OAuthAccountDto oAuthAccountDto = kakaoOAuthProvider.getAccountByOAuthToken(tokenByCode); // 카카오 계정 정보 DTO
		JwtTokens jwtTokens = userService.signUpOrLogin(oAuthAccountDto, KAKAO); // 액세스, 리프래쉬 토큰 2개를 jwtTokens에 동시에 바인딩.

		addJwtCookiesToResponse(jwtTokens, response); // 액세스 쿠키, 리프래쉬 쿠기 2개를 생성하고 응답에 담는 메소드
		return new ResponseEntity<>("login success", HttpStatus.OK);
	}

	@GetMapping("/auth/naver/callback")
	public ResponseEntity<String> naverCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) {
		OAuthTokenDto tokenByCode = naverOAuthProvider.getTokenByCode(code, state);
		OAuthAccountDto oAuthAccountDto = naverOAuthProvider.getAccountByOAuthToken(tokenByCode);
		JwtTokens jwtTokens = userService.signUpOrLogin(oAuthAccountDto, NAVER);

		addJwtCookiesToResponse(jwtTokens, response);
		return new ResponseEntity<>("login success", HttpStatus.OK);
	}

	// 로그인 성공 시 쿠키에 액세스, 리프레쉬 토큰 포함 응답.
	private void addJwtCookiesToResponse(JwtTokens jwtTokens, HttpServletResponse response) {
		Cookie accessCookie = cookieUtil.generateAccessCookie(jwtTokens.getAccessToken());
		Cookie refreshCookie = cookieUtil.generateRefreshCookie(jwtTokens.getRefreshToken());
		response.addCookie(accessCookie);
		response.addCookie(refreshCookie);
	}
}

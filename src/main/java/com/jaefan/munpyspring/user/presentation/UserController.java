package com.jaefan.munpyspring.user.presentation;

import static org.springframework.http.HttpStatus.*;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaefan.munpyspring.common.util.CookieUtil;
import com.jaefan.munpyspring.user.application.UserService;
import com.jaefan.munpyspring.user.presentation.dto.RefreshTokenRequestDto;
import com.jaefan.munpyspring.user.presentation.dto.UserSignUpRequestDto;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	private final CookieUtil cookieUtil;

	@PostMapping
	public ResponseEntity<String> signUp(@Valid @ModelAttribute UserSignUpRequestDto userSignUpRequestDto) throws
		IOException {
		userService.signUp(userSignUpRequestDto);
		return new ResponseEntity<>("signUp Success", CREATED);
	}

	@PostMapping("/refresh")
	public ResponseEntity<String> refreshAccessToken(@RequestBody RefreshTokenRequestDto refreshToken, HttpServletResponse response) {
		try {
			String newAccessToken = userService.refreshAccessToken(refreshToken.getRefreshToken());
			Cookie accessCookie = cookieUtil.generateAccessCookie(newAccessToken);
			response.addCookie(accessCookie);
			return new ResponseEntity<>("Access token refreshed successfully.", OK);
		} catch (JwtException ex) {
			return new ResponseEntity<>("Refresh token is invalid please login again.", UNAUTHORIZED);
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletResponse response) {
		// "access_token"과 "refresh_token" 쿠키를 삭제
		Cookie accessTokenCookie = cookieUtil.deleteCookie("access_token");
		Cookie refreshTokenCookie = cookieUtil.deleteCookie("refresh_token");

		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);

		return new ResponseEntity<>("Logout success", OK);
	}
}

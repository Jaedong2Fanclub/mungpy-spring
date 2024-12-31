package com.jaefan.munpyspring.security.filter.handler;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jaefan.munpyspring.common.util.CookieUtil;
import com.jaefan.munpyspring.security.domain.model.JwtTokens;
import com.jaefan.munpyspring.security.provider.JwtProvider;
import com.jaefan.munpyspring.user.domain.model.User;
import com.jaefan.munpyspring.user.domain.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	private final JwtProvider jwtProvider;

	private final CookieUtil cookieUtil;

	private final UserRepository userRepository;

	/**
	 * JsonLoginFilter 인증 성공 시 동작하는 핸들러 메소드로 액세스, 리프래쉬 토큰을 발급해 쿠키에 HttpOnly로 담아 응답합니다.
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

		String email = authentication.getName();

		JwtTokens jwtTokens = jwtProvider.createJwtTokensWithEmail(email); // jwtTokens: 액세스, 리프래쉬를 담는 객체

		updateVisitAt(email); // 로그인 성공 시 최근 접속 정보 업데이트

		Cookie accessTokenCookie = cookieUtil.generateAccessCookie(jwtTokens.getAccessToken());
		Cookie refreshTokenCookie = cookieUtil.generateRefreshCookie(jwtTokens.getRefreshToken());

		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);

		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("Login Success.");
	}

	@Transactional
	private void updateVisitAt(String email) {
		Optional<User> optionalUser = userRepository.findByEmail(email);

		User user = optionalUser.get();

		user.updateLastVisit();

		userRepository.save(user);
	}
}

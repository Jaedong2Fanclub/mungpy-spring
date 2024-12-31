package com.jaefan.munpyspring.security.filter;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Json 로그인 요청을 처리하는 필터
 */
@Component
@RequiredArgsConstructor
public class JsonLoginFilter extends OncePerRequestFilter {

	private final AuthenticationManager authenticationManager; // 인증 처리해주는 클래스

	private final AuthenticationSuccessHandler successHandler;

	private final AuthenticationFailureHandler failureHandler;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// 필터 체인을 통과하는 요청이 POST "/login"이면 필터가 동작
		if (!request.getRequestURI().equals("/login") || !request.getMethod().equalsIgnoreCase("POST")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
			String email = requestBody.get("email");
			String password = requestBody.get("password");

			if (email == null || password == null) {
				throw new IllegalArgumentException();
			}

			Authentication authentication = new UsernamePasswordAuthenticationToken(email, password); // 인증 객체 생성
			Authentication result = authenticationManager.authenticate(authentication); // 인증 성공 시 AuthenticationException가 발생하지 않음
			successHandler.onAuthenticationSuccess(request, response, result); // LoginSuccessHandler 호춯

		} catch (AuthenticationException ex) {	// email은 맞지만 비밀번호가 틀린경우
			failureHandler.onAuthenticationFailure(request, response, ex); // LoginFailureHandler 호출

		} catch (EntityNotFoundException ex) { // 없는 email인 경우
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.getWriter().write("입력하신 계정 정보가 존재하지 않습니다.");

		} catch (Exception ex) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("입력 형식을 다시 한번 확인해주세요.");
		}
	}
}

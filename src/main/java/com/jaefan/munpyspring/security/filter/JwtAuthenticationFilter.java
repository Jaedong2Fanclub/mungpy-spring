package com.jaefan.munpyspring.security.filter;

import static jakarta.servlet.http.HttpServletResponse.*;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jaefan.munpyspring.security.provider.JwtProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 요청에 액세스 토큰이 존재하면 토큰 인증을 실행하는 필터.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	/**
	 * 해당 필터는 JWT Bearer 토큰이 요청에 포함돼 있으면 무조건 동작.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String token = getJwtFromRequest(request);
		try {
			if (token != null && jwtProvider.isValid(token)) {
				String email = jwtProvider.getEmail(token);

				if (email != null) {
					Authentication authentication = jwtProvider.getAuthentication(token); // 토큰을 기반으로 인증 객체를 만드는 메소드
					SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 처리
				}
			}
		} catch (ExpiredJwtException ex) { // 액세스 토큰이 만료된 경우 /api/refresh로 리다이렉트 요구
			createErrorResponse(SC_UNAUTHORIZED, "Access token has expired. Redirect to /api/refresh", response);
			return;

		} catch (JwtException | IllegalArgumentException ex) { // 액세스 토큰이 유효하지 않은 토큰인 경우 로그인 요구
			createErrorResponse(SC_UNAUTHORIZED, "Access token is invalid please login.", response);
			return;
		}

		filterChain.doFilter(request, response); // 다음 필터로 요청 전달
	}

	private String getJwtFromRequest(HttpServletRequest request) { // 요청에서 Jwt 파싱
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	private void createErrorResponse(int status, String message, HttpServletResponse response) throws IOException {
		response.setStatus(status);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(message);
	}
}

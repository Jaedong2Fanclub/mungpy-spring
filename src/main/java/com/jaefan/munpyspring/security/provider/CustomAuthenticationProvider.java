package com.jaefan.munpyspring.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.jaefan.munpyspring.security.domain.model.CustomUserDetails;
import com.jaefan.munpyspring.security.domain.repository.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

/**
 * WebSecurityConfig에 등록되는 인증 구현체.
 * 인증이 성공하면 사용자 정보를 기반으로 인증객체인 UsernamePasswordAuthenticationToken을 생성하여 반환합니다.
 * 이 클래스에서 반환해주는 인증객체를 Spring Security Context Holder에 넣어주면 인증완료.
 */
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final UserDetailsServiceImpl userDetailsService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = (String)authentication.getCredentials();

		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(email);

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("입력하신 계정 정보가 올바르지 않습니다.");
		}

		return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}

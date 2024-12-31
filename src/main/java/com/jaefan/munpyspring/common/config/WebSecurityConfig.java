package com.jaefan.munpyspring.common.config;

import static org.springframework.http.HttpMethod.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jaefan.munpyspring.security.domain.repository.UserDetailsServiceImpl;
import com.jaefan.munpyspring.security.filter.JsonLoginFilter;
import com.jaefan.munpyspring.security.filter.JwtAuthenticationFilter;
import com.jaefan.munpyspring.security.provider.CustomAuthenticationProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final AuthenticationSuccessHandler successHandler;
	private final AuthenticationFailureHandler failureHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() { // 비밀번호 암호화 저장 또는 로그인 요청 비밀번호와 암호화 저장된 사용자 비밀번호 비교 시 사용됨.
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() { // 커스텀 UserDetailsService를 authenticationProvider에 등록
		return new CustomAuthenticationProvider(userDetailsServiceImpl, passwordEncoder());
	}

	@Bean
	public AuthenticationManager authenticationManager(){ // 커스텀 AuthenticationProvider를 authenticationManager에 등록
		return new ProviderManager(authenticationProvider());
	}

	@Bean
	public JsonLoginFilter jsonLoginFilter() {
		return new JsonLoginFilter(authenticationManager(), successHandler, failureHandler);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { // 필터 순서: JsonLoginFilter -> JwtAuthenticationFilter -> UsernamePasswordAuthenticationFilter(비활성화)
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(POST, "/api/animal").hasRole("SHELTER") // 동물 등록은 보호소만 가능.
				.anyRequest()
				.permitAll())
			.formLogin(form -> form.disable())
			.addFilterBefore(jsonLoginFilter(), UsernamePasswordAuthenticationFilter.class) // Json 로그인 요청을 처리하는 필터
			.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class); // 요청에 액세스 토큰이 포함되면 토큰 인증을 처리하는 필터

		return http.build();
	}
}

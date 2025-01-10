package com.jaefan.munpyspring.security.provider;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.jaefan.munpyspring.security.domain.model.CustomUserDetails;
import com.jaefan.munpyspring.security.domain.model.JwtTokens;
import com.jaefan.munpyspring.security.domain.repository.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

/**
 * JWT 토큰 발급 및 파싱을 담당하는 컴포넌트.
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final UserDetailsServiceImpl userDetailsService;

	@Value("${spring.jwt.secret}")
	private String secretKey;

	private long accessTokenValidTime = 30 * 60 * 1000L; // 30분

	private long refreshTokenValidTime = 2 * 7 * 24 * 60 * 60 * 1000L; // 2주

	public String generateAccessToken(String email) {
		return createToken(email, accessTokenValidTime);
	}

	public String generateRefreshToken(String email) {
		return createToken(email, refreshTokenValidTime);
	}

	public JwtTokens createJwtTokensWithEmail(String email) {
		String accessToken = generateAccessToken(email);
		String refreshToken = generateRefreshToken(email);
		return new JwtTokens(accessToken, refreshToken);
	}

	public String refreshAccessToken(String refreshToken) { // 리프래쉬 토큰이 유효하면 액세스 토큰을 재발급하는 메소드
		try {
			if (isValid(refreshToken)) {
				String email = getEmail(refreshToken);
				return generateAccessToken(email);
			} else {
				throw new JwtException("token is invalid");
			}
		} catch (Exception ex) {
			throw new JwtException("token is invalid");
		}
	}

	private String createToken(String email, Long age) {
		return Jwts.builder()
			.setSubject(email)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + age))
			.claim("email", email)
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	public boolean isValid(String token) { // 이 함수는 토큰이 유효한 경우 true를 반환하고 아닌 경우 예외가 발생합니다. 이 함수를 호출하는 쪽에서 예외를 catch해 적절히 응답해야 합니다. ex) JwtAuthenticationFilter의 doFilterInternal
		Jws<Claims> claimsJws = Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token);
		return true;
	}

	public String getEmail(String token) {
		Jws<Claims> claimsJws = getClaims(token);
		return claimsJws.getBody().getSubject();
	}

	public Jws<Claims> getClaims(String token) {
		return Jwts.parser()
			.setSigningKey(secretKey)
			.parseClaimsJws(token);
	}

	/**
	 * 토큰을 기반으로 인증 객체를 만드는 메소드. 토큰이 유효한 경우에만 동작.
	 */
	public Authentication getAuthentication(String token) {
		CustomUserDetails userDetails = (CustomUserDetails)userDetailsService.loadUserByUsername(getEmail(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
}

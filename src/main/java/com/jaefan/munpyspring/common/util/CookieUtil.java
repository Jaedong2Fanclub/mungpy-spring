package com.jaefan.munpyspring.common.util;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;

@Component
public class CookieUtil {

	private final int accessCookieAge = 30 * 60;
	private final int refreshCookieAge = 2 * 7 * 24 * 60 * 60;

	public Cookie generateAccessCookie(String accessToken) {
		return generateCookie("access_token", accessToken, accessCookieAge);
	}

	public Cookie generateRefreshCookie(String refreshToken) {
		return generateCookie("refresh_token", refreshToken, refreshCookieAge);
	}

	private Cookie generateCookie(String name, String content, int age) {
		Cookie cookie = new Cookie(name, content);
		cookie.setMaxAge(age);
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		return cookie;
	}

	public Cookie deleteCookie(String name) {
		Cookie cookie = new Cookie(name,null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		return cookie;
	}
}

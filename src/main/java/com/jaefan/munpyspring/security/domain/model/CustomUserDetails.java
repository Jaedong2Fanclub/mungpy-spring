package com.jaefan.munpyspring.security.domain.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jaefan.munpyspring.user.domain.model.Role;
import com.jaefan.munpyspring.user.domain.model.User;

import lombok.Getter;

/**
 * 인증에 필요한 값을 담는 객체
 */
@Getter
public class CustomUserDetails implements UserDetails {

	private String email;
	private String password;
	private Role role;

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
	}

	public CustomUserDetails(User user) {
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.role = user.getRole();
	}
}

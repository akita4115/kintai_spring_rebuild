package com.example.demo.domain.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.domain.model.LoginUser;

public class LoginUserDetails implements UserDetails {

	private final LoginUser loginUser;

	public LoginUserDetails(LoginUser loginUser) {
		this.loginUser = loginUser;

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		if ("1".equals(loginUser.getRoleCd())) {
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));

		}
		return List.of(new SimpleGrantedAuthority("ROLE_GENERAL"));
	}

	@Override
	public String getPassword() {
		return loginUser.getPassword();

	}

	@Override
	public String getUsername() {
		return loginUser.getEmail();
	}

	public LoginUser getLoginUser() {
		return loginUser;
	}
}

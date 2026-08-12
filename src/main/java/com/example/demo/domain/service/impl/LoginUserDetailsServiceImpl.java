package com.example.demo.domain.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.LoginUser;
import com.example.demo.domain.service.LoginUserDetails;
import com.example.demo.domain.service.LoginUserService;

@Service
public class LoginUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private LoginUserService loginUserService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		LoginUser loginUser = loginUserService.findByEmail(email);

		if (loginUser == null) {
			throw new UsernameNotFoundException("ユーザが見つかりません。");
		}

		return new LoginUserDetails(loginUser);
	}
}
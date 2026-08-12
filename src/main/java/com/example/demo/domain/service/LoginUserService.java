package com.example.demo.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.LoginUser;
import com.example.demo.repository.UserMapper;

@Service
public class LoginUserService {

	@Autowired
	private UserMapper userMapper;
	
	public LoginUser findByUserNo(String userNo) {
		return userMapper.findByUserNo(userNo);
	}

	public LoginUser findByEmail(String email) {
		return userMapper.findByEmail(email);
	}
}

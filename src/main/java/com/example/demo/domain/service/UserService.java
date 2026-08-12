package com.example.demo.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.LoginUser;
import com.example.demo.form.LoginUserSearchForm;
import com.example.demo.repository.UserMapper;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// 社員一覧取得
	public List<LoginUser> getUserList(LoginUserSearchForm form) {
		return userMapper.findAll(form);
	}

	// 検索条件に合致する総件数を取得
	public int getUserCount(LoginUserSearchForm form) {
		return userMapper.countAll(form);
	}

	// 社員登録
	public int createUser(LoginUser user) {

		// パスワードを暗号化
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userMapper.insertOne(user);
	}

	// 社員更新（パスワードが指定された場合のみ暗号化して更新）
	public int updateUser(LoginUser user) {
		if (user.getPassword() != null && !user.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		} else {
			// パスワード未入力の場合は更新しない（XML側のif条件で除外）
			user.setPassword(null);
		}

		return userMapper.updateOne(user);
	}

	// 社員削除
	public int deleteUser(Long id) {
		return userMapper.deleteOne(id);
	}

	// メールアドレスから社員を取得
	public LoginUser findByEmail(String email) {
		return userMapper.findByEmail(email);
	}
}
package com.example.demo.domain.entity;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.model.LoginUser;

import lombok.Data;

@Data
public class UserEntity {

	private LoginUser user;  // 一件取得・更新・削除で使用

	private List<LoginUser> userList;  // 一覧取得で使用

	private int totalCount;  // 検索結果の総件数（ページング用）

	private Map<String, String> errors;  // エラーメッセージ
}
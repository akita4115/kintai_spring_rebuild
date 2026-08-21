package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.model.LoginUser;
import com.example.demo.form.UserSearchForm;
@Mapper
public interface UserMapper {

	LoginUser findByUserNo(String userNo);

	LoginUser findByEmail(String email);

	List<LoginUser> findAll(UserSearchForm form);

	int countAll(UserSearchForm form);

	int insertOne(LoginUser user);

	int updateOne(LoginUser user);

	int deleteOne(Long id);
}
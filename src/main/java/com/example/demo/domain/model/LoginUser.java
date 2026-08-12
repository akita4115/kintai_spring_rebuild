package com.example.demo.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class LoginUser {

	private Long id;

	private String userNo;

	private String name;

	private String email;

	private LocalDate startDate;

	@JsonIgnore
	private String password;

	private String roleCd;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
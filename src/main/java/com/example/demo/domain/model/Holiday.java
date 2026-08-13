package com.example.demo.domain.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Holiday {

	// ID
	private Long id;

	// 日付
	private LocalDate yyyymmdd;

	// 祝日名
	private String holidayName;

}
package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HolidayDetailForm {

	// 日付
	@NotBlank(message = "{require_check}")
	private String yyyymmdd;

	// 祝日名
	@NotBlank(message = "{require_check}")
	private String holidayName;
}
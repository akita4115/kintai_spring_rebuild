package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 祝日更新フォーム
 */
@Data
public class HolidayUpdateForm {

	// ID
	@NotNull(message = "{require_check}")
	private Long id;

	// 日付
	@NotBlank(message = "{require_check}")
	@Size(max = 20, message = "{holiday_date_size}")
	private String yyyymmdd;

	// 祝日名
	@NotBlank(message = "{require_check}")
	@Size(max = 10, message = "{holiday_name_size}")
	private String holidayName;
}
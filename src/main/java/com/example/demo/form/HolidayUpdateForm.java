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
	@NotNull(message = "{holiday_id_required}")
	private Long id;

	// 日付
	@NotBlank(message = "{holiday_date_required}")
	private String yyyymmdd;

	// 祝日名
	@NotBlank(message = "{holiday_name_required}")
	@Size(max = 10, message = "{holiday_name_size}")
	private String holidayName;
}
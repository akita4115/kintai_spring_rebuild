package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HolidayDetailForm {
	
	//ID
	private Integer id;

	// 日付
	@NotBlank(message = "{holiday_date_required}")
	private String yyyymmdd;

	// 祝日名
	@NotBlank(message = "{holiday_name_required}")
	@Size(max = 10, message = "{holiday_name_size}")
	private String holidayName;
}
package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateForm {

	// ID
	@NotNull(message = "{user_id_required}")
	private Long id;

	// 社員番号
	@NotBlank(message = "{user_no_required}")
	@Size(max = 10, message = "{user_no_size}")
	private String userNo;

	// 社員名
	@NotBlank(message = "{user_name_required}")
	@Size(max = 255, message = "{user_name_size}")
	private String name;

	// 入社日
	@NotBlank(message = "{start_date_required}")
	private String startDate;

	// パスワード（入力時のみ）
	@Size(max = 255, message = "{password_size}")
	private String password;

	// 確認用パスワード
	private String confirmPassword;

	// 権限コード
	@NotBlank(message = "{role_cd_required}")
	@Pattern(regexp = "^$|^[01]$", message = "{role_cd_format}")
	private String roleCd;
}
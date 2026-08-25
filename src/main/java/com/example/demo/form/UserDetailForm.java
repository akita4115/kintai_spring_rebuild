package com.example.demo.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDetailForm {

	// Id
	private Long id;

	// 社員番号
	@NotBlank(message = "{user_no_required}")
	@Size(max = 10, message = "{user_no_size}")
	private String userNo;

	// 社員名
	@NotBlank(message = "{user_name_required}")
	@Size(max = 255, message = "{user_name_size}")
	private String name;

	// メールアドレス
	@NotBlank(message = "{email_required}")
	@Email(message = "{email_format}")
	@Size(max = 255, message = "{email_size}")
	private String email;

	// 入社日
	@NotBlank(message = "{start_date_required}")
	private String startDate;

	// パスワード
	@NotBlank(message = "{password_required}")
	@Size(max = 255, message = "{password_size}")
	private String password;

	// 確認用パスワード
	@NotBlank(message = "{confirm_password_required}")
	private String confirmPassword;

	// 権限コード
	@NotBlank(message = "{role_cd_required}")
	@Pattern(regexp = "^$|^[01]$", message = "{role_cd_format}")
	private String roleCd;
}
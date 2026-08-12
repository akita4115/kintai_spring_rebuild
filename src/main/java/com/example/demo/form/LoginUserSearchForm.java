package com.example.demo.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class LoginUserSearchForm {

	// 社員番号
	private String userNo;

	// 名前
	private String name;

	// メール
	private String email;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;

	// 権限
	private String roleCd;

	/** 現在のページ番号（1から） */
	private int page = 1;

	/** 1ページあたりの件数 */
	private final int pageSize = 5;

	public int getPageSize() {
		return pageSize;
	}

	/** LIMIT句のOFFSET値を算出 */
	public int getOffset() {
		return (page - 1) * pageSize;
	}
}
package com.example.demo.form;

import lombok.Data;

@Data
public class HolidaySearchForm {

	//日付
	private String yyyymmdd;
	
	//祝日名
	private String holidayName;
	
	//ページ番号
	private int page = 1;
	
	//1ページの表示件数
	private int pageSize = 5;
	
	//SQL用オフセット
	public int getOffset() {
		return (page - 1) * pageSize;
	}
	
}

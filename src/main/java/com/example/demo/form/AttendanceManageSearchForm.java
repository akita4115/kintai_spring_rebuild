package com.example.demo.form;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class AttendanceManageSearchForm {

	//年月
	private String targetMonth = 
		YearMonth.now().format(
				DateTimeFormatter.ofPattern("yyyy-MM"));
	
	//社員番号
	private String employeeCode;
	
	 // 社員名
    private String employeeName;

    // ステータス
    private String status;

    // 現在のページ
    private int page = 1;

    // 1ページ当たりの表示件数
    private int pageSize = 5;
    
 
    //SQLの検索開始位置を計算
    public int getOffset() {
        return (page - 1) * pageSize;
    }
    
}
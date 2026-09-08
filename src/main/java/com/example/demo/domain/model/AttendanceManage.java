package com.example.demo.domain.model;

import lombok.Data;

@Data
public class AttendanceManage {

	//勤怠ヘッダーID
	private Long attendanceHeadId;
	
	// 年月
    private String targetMonth;

    // 社員番号
    private String employeeCode;

    // 社員名
    private String employeeName;

    // ステータスコード
    private String status;

    // ステータス名
    private String statusName;
}

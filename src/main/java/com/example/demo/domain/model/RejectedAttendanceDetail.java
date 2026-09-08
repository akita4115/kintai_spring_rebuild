package com.example.demo.domain.model;

import lombok.Data;

/**
 * 差戻勤怠情報
 */

@Data
public class RejectedAttendanceDetail {

	//差戻年月
	private String rejectedMonth;
	
	//差戻理由
	private String rejectedReason;
	
}

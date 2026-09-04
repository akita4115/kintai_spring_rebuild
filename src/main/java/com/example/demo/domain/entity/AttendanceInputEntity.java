package com.example.demo.domain.entity;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.model.AttendanceInputDetail;

import lombok.Data;

@Data
public class AttendanceInputEntity {

	//表示対象年月
	private String targetMonth;

	//申請状態
	private String statusCd;

	//差戻年月
	private String rejectedMonth;

	//差戻理由
	private String rejectedReason;

	//一か月分の勤怠データ
	private List<AttendanceInputDetail> attendanceList;

	//エラーメッセージ
	private Map<String, String> errors;

}

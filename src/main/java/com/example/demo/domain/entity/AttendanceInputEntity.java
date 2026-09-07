package com.example.demo.domain.entity;

import java.util.List;
import java.util.Map;

import com.example.demo.domain.model.AttendanceInputDetail;
import com.example.demo.domain.model.RejectedAttendanceDetail;

import lombok.Data;

@Data
public class AttendanceInputEntity {

	//表示対象年月
	private String targetMonth;

	//申請状態
	private String statusCd;

	//差戻中の勤怠情報
	private List<RejectedAttendanceDetail> rejectedAttendanceList;

	//一か月分の勤怠データ
	private List<AttendanceInputDetail> attendanceList;

	//エラーメッセージ
	private Map<String, String> errors;

}

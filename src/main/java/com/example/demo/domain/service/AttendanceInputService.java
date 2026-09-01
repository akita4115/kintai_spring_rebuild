package com.example.demo.domain.service;

import java.time.YearMonth;
import java.util.List;

import com.example.demo.domain.model.AttendanceInputDetail;

/**
 * 勤怠入力のサービス
 */
public interface AttendanceInputService {

	
	 // 指定年月の勤怠入力一覧を取得

	public List<AttendanceInputDetail> getAttendanceList(
			String email,
			YearMonth targetMonth);
	
	//勤怠情報を保存する
	public void saveAttendance(
			String email,
			YearMonth targetMonth,
			List<AttendanceInputDetail> attendanceList);
	
	
	//指定年月の勤怠ステータスを取得する
	public String getAttendanceStatus(
			String email,
			YearMonth targetMonth);

}
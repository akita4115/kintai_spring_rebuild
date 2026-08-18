package com.example.demo.domain.service;

import java.time.YearMonth;
import java.util.List;

import com.example.demo.domain.model.AttendanceInputDetail;

/**
 * 勤怠入力サービス
 */
public interface AttendanceInputService {

	/**
	 * 指定年月の勤怠入力一覧を取得
	 *
	 * @param targetMonth 表示対象年月
	 * @return 1か月分の勤怠入力一覧
	 */
	public List<AttendanceInputDetail> getAttendanceList(
			YearMonth targetMonth);
}
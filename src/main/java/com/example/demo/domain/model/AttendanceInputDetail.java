package com.example.demo.domain.model;

import lombok.Data;

/**
 * 勤怠入力画面の1日分のデータ
 */

@Data
public class AttendanceInputDetail {
	

	//日付
	private String attendanceDate;
	
	//曜日
	private String dayOfWeek;
	
	//区分
	private String attendanceType;
	
	//開始時刻
	private String startTime;
	
	//終了時刻
	private String endTime;
	
	//昼休憩時間
	private String breakTime;
	
	//夜休憩時間
	private String nightBreakTime;
	
	//勤務時間
	private String workTime;
	
	//残業時間
	private String overTime;
	
	//備考
	private String remarks;
		
	
}

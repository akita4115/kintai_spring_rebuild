package com.example.demo.domain.service;

import java.util.List;

import com.example.demo.domain.model.AttendanceManage;
import com.example.demo.form.AttendanceManageSearchForm;
/**
 * 勤怠管理サービス
 */
public interface AttendanceManageService {

	//勤怠一覧を取得
	public List<AttendanceManage> getAttendanceManageList(
			AttendanceManageSearchForm searchForm);


	//勤怠一覧の件数を取得
	public int getAttendanceManageCount(
			AttendanceManageSearchForm searchForm);

	
    //選択された勤怠を承認する
    public void approveAttendances(
            List<Long> attendanceHeadIds);
    
    
}

package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.model.AttendanceInputDetail;
import com.example.demo.domain.model.AttendanceManage;
import com.example.demo.domain.model.RejectedAttendanceDetail;
import com.example.demo.form.AttendanceManageSearchForm;

/**
 * 勤怠情報Mapper
 */
@Mapper
public interface AttendanceMapper {

	//メールアドレスからユーザーIDを取得する
	public Long findUserIdByEmail(
			@Param("email") String email);

	
	//ユーザーIDと年月から勤怠ヘッダーIDを取得する
	public Long findAttendanceHeadId(
			@Param("userId") Long userId,
			@Param("yyyymm") String yyyymm);

	
	//勤怠ヘッダーを登録する
	public void insertAttendanceHead(
			@Param("userId") Long userId,
			@Param("yyyymm") String yyyymm,
			@Param("status") String status);

	
	//対象ヘッダーに紐づく勤怠明細を削除する
	public void deleteAttendanceDetails(
			@Param("attendanceHeadId") Long attendanceHeadId);


	//勤怠明細を登録する
	public void insertAttendanceDetails(
			@Param("attendanceHeadId") Long attendanceHeadId,
			@Param("attendanceList") List<AttendanceInputDetail> attendanceList);
	
	
	//勤怠ヘッダーのステータスを取得する
	public String findAttendanceHeadStatus(
			@Param("attendanceHeadId")
			Long attendanceHeadId);
	
	
	//勤怠明細を取得する
	public List<AttendanceInputDetail> findAttendanceDetails(
			@Param("attendanceHeadId")
			Long attendanceHeadId);
	
	
	//勤怠ステータスを更新する
	public void updateAttendanceStatus(
			@Param("attendanceHeadId")
			Long attendanceHeadId,
			@Param("status")
			String status);
	
	
	//差戻中の勤怠情報をすべて取得する
	public List<RejectedAttendanceDetail> findRejectedAttendances(
			@Param("userId")
			Long userId);
	
	
	//勤怠管理画面の検索結果を取得
	public List<AttendanceManage> findAttendanceManageList(
			AttendanceManageSearchForm SearchForm);
	
	
	//勤怠管理画面の検索結果件数を取得
	public int countAttendanceManageList(
	        AttendanceManageSearchForm searchForm);
	
	
	
}
package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.model.AttendanceInputDetail;

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
}
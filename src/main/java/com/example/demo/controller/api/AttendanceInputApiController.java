package com.example.demo.controller.api;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.entity.AttendanceInputEntity;
import com.example.demo.domain.model.AttendanceInputDetail;
import com.example.demo.domain.service.AttendanceInputService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/attendance/input")
@Slf4j
public class AttendanceInputApiController {

	@Autowired
	private AttendanceInputService attendanceInputService;

	/**
	 * GET 勤怠入力一覧取得
	 */
	@GetMapping
	public AttendanceInputEntity getAttendanceInput(
			@RequestParam(name = "targetMonth", required = false) String targetMonth, Authentication authentication) {

		AttendanceInputEntity attendanceInputEntity = new AttendanceInputEntity();

		try {
			YearMonth yearMonth;

			// 年月が指定されていない場合は現在年月
			if (targetMonth == null
					|| targetMonth.isBlank()) {

				yearMonth = YearMonth.now();

			} else {
				yearMonth = YearMonth.parse(targetMonth);
			}

			// Serviceから勤怠一覧を取得
			String email = authentication.getName();

			List<AttendanceInputDetail> attendanceList = attendanceInputService.getAttendanceList(
					email,
					yearMonth);

			attendanceInputEntity.setTargetMonth(
					yearMonth.toString());

			attendanceInputEntity.setAttendanceList(
					attendanceList);

			String statusCd = attendanceInputService.getAttendanceStatus(
					email,
					yearMonth);

			attendanceInputEntity.setStatusCd(statusCd);
			
			//差戻中の勤怠情報を取得
			AttendanceInputEntity rejectedAttendance = 
					attendanceInputService.getRejectedAttendance(email);
			
			if (rejectedAttendance != null) {
				attendanceInputEntity.setRejectedMonth(
						rejectedAttendance.getRejectedMonth());
				
				attendanceInputEntity.setRejectedReason(
						rejectedAttendance.getRejectedReason());
			}
			
			return attendanceInputEntity;
			
			

		} catch (DateTimeParseException ex) {
			log.error(
					"年月の形式が正しくありません。",
					ex);

			Map<String, String> errors = new HashMap<>();

			errors.put(
					"targetMonth",
					"年月の形式が正しくありません。");

			attendanceInputEntity.setErrors(errors);

			return attendanceInputEntity;

		} catch (Exception ex) {
			log.error(
					"勤怠入力データの取得に失敗しました。",
					ex);

			Map<String, String> errors = new HashMap<>();

			errors.put(
					"attendance",
					"勤怠入力データの取得に失敗しました。");

			attendanceInputEntity.setErrors(errors);

			return attendanceInputEntity;
		}
	}

	/**
	 * POST 勤怠情報保存
	 */
	@PostMapping("/save")
	public AttendanceInputEntity saveAttendance(
			@RequestBody AttendanceInputEntity request,
			Authentication authentication) {

		try {
			//ログイン中のユーザのメールアドレス
			String email = authentication.getName();

			//React	から受け取った年月をYearMonthに変換
			YearMonth targetMonth = YearMonth.parse(request.getTargetMonth());

			//serviceの保存処理を呼び出す
			attendanceInputService.saveAttendance(
					email,
					targetMonth,
					request.getAttendanceList());

			log.info(
					"勤怠保存完了：email={}, targetMonth={}",
					email,
					targetMonth);

			return request;

		} catch (DateTimeParseException ex) {
			log.error(
					"年月の形式が正しくありません。",
					ex);

			Map<String, String> errors = new HashMap<>();

			errors.put(
					"targetMonth",
					"年月の形式が正しくありません。");

			request.setErrors(errors);

			return request;

		} catch (Exception ex) {
			log.error(
					"勤怠情報の保存に失敗しました。",
					ex);

			Map<String, String> errors = new HashMap<>();

			errors.put(
					"attendance",
					"勤怠情報の保存に失敗しました。");

			request.setErrors(errors);

			return request;
		}

	}

	/**
	 * POST 勤怠情報申請
	 */
	@PostMapping("/apply")
	public AttendanceInputEntity applyAttendance(
			@RequestBody AttendanceInputEntity request,
			Authentication authentication) {

		try {
			// ログイン中のユーザーのメールアドレス
			String email = authentication.getName();

			// Reactから受け取った年月をYearMonthへ変換
			YearMonth targetMonth = YearMonth.parse(
					request.getTargetMonth());

			// Serviceの申請処理を呼び出す
			attendanceInputService.applyAttendance(
					email,
					targetMonth,
					request.getAttendanceList());

			// 申請後のステータス
			request.setStatusCd("1");

			log.info(
					"勤怠申請完了：email={}, targetMonth={}",
					email,
					targetMonth);

			return request;

		} catch (DateTimeParseException ex) {
			log.error(
					"年月の形式が正しくありません。",
					ex);

			Map<String, String> errors = new HashMap<>();

			errors.put(
					"targetMonth",
					"年月の形式が正しくありません。");

			request.setErrors(errors);

			return request;

		} catch (Exception ex) {
			log.error(
					"勤怠情報の申請に失敗しました。",
					ex);

			Map<String, String> errors = new HashMap<>();

			errors.put(
					"attendance",
					"勤怠情報の申請に失敗しました。");

			request.setErrors(errors);

			return request;
		}
	}
}

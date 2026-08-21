package com.example.demo.controller.api;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
			@RequestParam(
					name = "targetMonth",
					required = false)
			String targetMonth) {

		AttendanceInputEntity attendanceInputEntity =
				new AttendanceInputEntity();

		try {
			YearMonth yearMonth;

			// 年月が指定されていない場合は現在年月
			if (targetMonth == null
					|| targetMonth.isBlank()) {

				yearMonth = YearMonth.now();

			} else {
				yearMonth =
						YearMonth.parse(targetMonth);
			}

			// Serviceから勤怠一覧を取得
			List<AttendanceInputDetail> attendanceList =
					attendanceInputService
						.getAttendanceList(yearMonth);

			attendanceInputEntity.setTargetMonth(
					yearMonth.toString());

			attendanceInputEntity.setAttendanceList(
					attendanceList);

			// DB未登録の場合は申請状態なし
			attendanceInputEntity.setStatusCd(null);

			return attendanceInputEntity;

		} catch (DateTimeParseException ex) {
			log.error(
					"年月の形式が正しくありません。",
					ex);

			Map<String, String> errors =
					new HashMap<>();

			errors.put(
					"targetMonth",
					"年月の形式が正しくありません。");

			attendanceInputEntity.setErrors(errors);

			return attendanceInputEntity;

		} catch (Exception ex) {
			log.error(
					"勤怠入力データの取得に失敗しました。",
					ex);

			Map<String, String> errors =
					new HashMap<>();

			errors.put(
					"attendance",
					"勤怠入力データの取得に失敗しました。");

			attendanceInputEntity.setErrors(errors);

			return attendanceInputEntity;
		}
	}
}
package com.example.demo.domain.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.model.AttendanceInputDetail;
import com.example.demo.domain.model.Holiday;
import com.example.demo.repository.HolidayMapper;

/**
 * 勤怠入力サービス実装
 */
@Service
@Transactional
public class AttendanceInputServiceImpl
		implements AttendanceInputService {

	@Autowired
	private HolidayMapper holidayMapper;

	/**
	 * 指定年月の勤怠入力一覧を取得
	 */
	@Override
	public List<AttendanceInputDetail> getAttendanceList(
			YearMonth targetMonth) {

		// 対象月の初日
		LocalDate startDate =
				targetMonth.atDay(1);

		// 対象月の末日
		LocalDate endDate =
				targetMonth.atEndOfMonth();

		// 対象月の祝日を取得
		List<Holiday> holidayList =
				holidayMapper.findByPeriod(
						startDate,
						endDate);

		// 日付をキーにして祝日名を取得できるようにする
		Map<LocalDate, String> holidayMap =
				new HashMap<>();

		for (Holiday holiday : holidayList) {

			holidayMap.put(
					holiday.getYyyymmdd(),
					holiday.getHolidayName());
		}

		List<AttendanceInputDetail> attendanceList =
				new ArrayList<>();

		// 対象月の1日から月末まで作成
		for (int day = 1;
				day <= targetMonth.lengthOfMonth();
				day++) {

			LocalDate date =
					targetMonth.atDay(day);

			AttendanceInputDetail detail =
					new AttendanceInputDetail();

			detail.setAttendanceDate(
					date.toString());

			detail.setDayOfWeek(
					getJapaneseDayOfWeek(
							date.getDayOfWeek()));

			boolean isSaturday =
					date.getDayOfWeek()
					== DayOfWeek.SATURDAY;

			boolean isSunday =
					date.getDayOfWeek()
					== DayOfWeek.SUNDAY;

			boolean isHoliday =
					holidayMap.containsKey(date);
			
			detail.setHoliday(isHoliday);

			// 土曜日・日曜日・祝日
			if (isSaturday
					|| isSunday
					|| isHoliday) {

				detail.setAttendanceType("休日");
				detail.setKbn("2");
				detail.setAttendanceType("休日");
				detail.setStartTime("");
				detail.setEndTime("");
				detail.setBreakTime("");
				detail.setNightBreakTime("");
				detail.setWorkTime("");
				detail.setOverTime("");

			} else {
				// 平日
				detail.setAttendanceType("出勤");
				detail.setKbn("1");
				detail.setAttendanceType("出勤");
				detail.setStartTime("09:00");
				detail.setEndTime("18:00");
				detail.setBreakTime("01:00");
				detail.setNightBreakTime("00:00");
				detail.setWorkTime("08:00");
				detail.setOverTime("00:00");
			}

			// 祝日の場合は祝日名を備考へ設定
			detail.setRemarks(
					holidayMap.getOrDefault(
							date,
							""));

			attendanceList.add(detail);
		}

		return attendanceList;
	}

	/**
	 * 曜日を日本語へ変換
	 */
	private String getJapaneseDayOfWeek(
			DayOfWeek dayOfWeek) {

		return switch (dayOfWeek) {
		case MONDAY -> "月";
		case TUESDAY -> "火";
		case WEDNESDAY -> "水";
		case THURSDAY -> "木";
		case FRIDAY -> "金";
		case SATURDAY -> "土";
		case SUNDAY -> "日";
		};
	}
}
package com.example.demo.domain.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.model.AttendanceInputDetail;
import com.example.demo.domain.model.Holiday;
import com.example.demo.domain.model.RejectedAttendanceDetail;
import com.example.demo.repository.AttendanceMapper;
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

	@Autowired
	private AttendanceMapper attendanceMapper;

	@Autowired
	private NationalHolidayService nationalHolidayService;

	/**
	 * 指定年月の勤怠入力一覧を取得
	 */
	@Override
	public List<AttendanceInputDetail> getAttendanceList(
			String email,
			YearMonth targetMonth) {

		Long userId = attendanceMapper.findUserIdByEmail(email);

		if (userId == null) {
			throw new IllegalArgumentException(
					"ログインユーザーが見つかりません。");
		}

		String yyyymm = targetMonth.format(
				DateTimeFormatter.ofPattern("yyyyMM"));

		Long attendanceHeadId = attendanceMapper.findAttendanceHeadId(userId, yyyymm);

		// 祝日マスタから祝日を取得する
		LocalDate startDate = targetMonth.atDay(1);
		LocalDate endDate = targetMonth.atEndOfMonth();

		List<Holiday> holidayList = holidayMapper.findByPeriod(startDate, endDate);

		// 外部APIから国民の祝日を取得する
		Map<LocalDate, String> holidayMap = new HashMap<>(
				nationalHolidayService.getNationalHolidays(
						targetMonth));

		// 祝日マスタの内容を追加する
		for (Holiday holiday : holidayList) {
			holidayMap.put(
					holiday.getYyyymmdd(),
					holiday.getHolidayName());
		}

		// 1か月分の初期値を作成する
		List<AttendanceInputDetail> attendanceList = new ArrayList<>();

		for (int day = 1; day <= targetMonth.lengthOfMonth(); day++) {

			LocalDate date = targetMonth.atDay(day);

			AttendanceInputDetail detail = new AttendanceInputDetail();

			detail.setAttendanceDate(date.toString());

			detail.setDayOfWeek(
					getJapaneseDayOfWeek(date.getDayOfWeek()));

			boolean isSaturday = date.getDayOfWeek() == DayOfWeek.SATURDAY;

			boolean isSunday = date.getDayOfWeek() == DayOfWeek.SUNDAY;

			boolean isHoliday = holidayMap.containsKey(date);

			detail.setHoliday(isHoliday);

			if (isSaturday
					|| isSunday
					|| isHoliday) {

				detail.setKbn("2");
				detail.setAttendanceType("休日");
				detail.setStartTime("");
				detail.setEndTime("");
				detail.setBreakTime("");
				detail.setNightBreakTime("");
				detail.setWorkTime("");
				detail.setOverTime("");

			} else {

				detail.setKbn("1");
				detail.setAttendanceType("出勤");
				detail.setStartTime("09:00");
				detail.setEndTime("18:00");
				detail.setBreakTime("01:00");
				detail.setNightBreakTime("00:00");
				detail.setWorkTime("08:00");
				detail.setOverTime("00:00");
			}

			detail.setRemarks(
					holidayMap.getOrDefault(date, ""));

			attendanceList.add(detail);
		}

		// DBに保存済みの明細があれば初期値へ上書きする
		if (attendanceHeadId != null) {

			List<AttendanceInputDetail> savedList = attendanceMapper.findAttendanceDetails(
					attendanceHeadId);

			for (AttendanceInputDetail savedDetail : savedList) {

				int day = Integer.parseInt(
						savedDetail.getAttendanceDate());

				AttendanceInputDetail detail = attendanceList.get(day - 1);

				detail.setKbn(savedDetail.getKbn());

				detail.setAttendanceType(
						getAttendanceType(savedDetail.getKbn()));

				detail.setStartTime(
						savedDetail.getStartTime());

				detail.setEndTime(
						savedDetail.getEndTime());

				detail.setBreakTime(
						savedDetail.getBreakTime());

				detail.setNightBreakTime(
						savedDetail.getNightBreakTime());

				detail.setWorkTime(
						savedDetail.getWorkTime());

				detail.setOverTime(
						savedDetail.getOverTime());

				// 保存済みの備考で上書きする
				detail.setRemarks(
						savedDetail.getRemarks() == null
								? ""
								: savedDetail.getRemarks());
			}
		}

		return attendanceList;
	}

	/**
	 * 勤怠情報を保存する
	 */
	@Override
	public void saveAttendance(
			String email,
			YearMonth targetMonth,
			List<AttendanceInputDetail> attendanceList) {

		Long userId = attendanceMapper.findUserIdByEmail(email);

		if (userId == null) {
			throw new IllegalArgumentException(
					"ログインユーザーが見つかりません。");
		}

		String yyyymm = targetMonth.format(
				DateTimeFormatter.ofPattern("yyyyMM"));

		Long attendanceHeadId = attendanceMapper.findAttendanceHeadId(userId, yyyymm);
		
		
		// 新規登録か更新かを判定する
		boolean isNewAttendance =
		        attendanceHeadId == null;


		// 申請中・承認済みの勤怠は変更不可
		if (attendanceHeadId != null) {

			String status = attendanceMapper.findAttendanceHeadStatus(
					attendanceHeadId);

			if ("1".equals(status)
					|| "3".equals(status)) {

				throw new IllegalStateException(
						"申請中または承認済みの勤怠は変更できません。");
			}
		}

		// 勤怠ヘッダーが未登録の場合
		if (attendanceHeadId == null) {

			attendanceMapper.insertAttendanceHead(userId, yyyymm, "0");

			attendanceHeadId = attendanceMapper.findAttendanceHeadId(
					userId, yyyymm);
		}

		if (attendanceHeadId == null) {
			throw new IllegalStateException(
					"勤怠ヘッダーの登録に失敗しました。");
		}

		// 画面から受け取った勤怠明細を登録または更新する
		if (attendanceList != null
				&& !attendanceList.isEmpty()) {

			if (isNewAttendance) {

				attendanceMapper.insertAttendanceDetails(
						attendanceHeadId, attendanceList);

			} else {

				for (AttendanceInputDetail attendance : attendanceList) {

					attendanceMapper.updateAttendanceDetail(
							attendanceHeadId, attendance);
				}
			}
		}

		// 勤怠ヘッダーの更新日時を更新する
		attendanceMapper.updateAttendanceHeadUpdatedAt(
				attendanceHeadId);

	}

	/**
	 * 勤怠情報を申請する
	 */
	@Override
	public void applyAttendance(
			String email,
			YearMonth targetMonth,
			List<AttendanceInputDetail> attendanceList) {

		// 保存処理を共通利用する
		saveAttendance(email, targetMonth, attendanceList);

		Long userId = attendanceMapper.findUserIdByEmail(email);

		if (userId == null) {
			throw new IllegalArgumentException(
					"ログインユーザーが見つかりません。");
		}

		String yyyymm = targetMonth.format(
				DateTimeFormatter.ofPattern("yyyyMM"));

		Long attendanceHeadId = attendanceMapper.findAttendanceHeadId(userId, yyyymm);

		if (attendanceHeadId == null) {
			throw new IllegalStateException(
					"勤怠ヘッダーが見つかりません。");
		}

		// 申請固有処理：ステータスを申請中へ変更する
		attendanceMapper.updateAttendanceStatus(
				attendanceHeadId, "1");
	}

	/**
	 * 指定年月の勤怠ステータスを取得する
	 */
	@Override
	public String getAttendanceStatus(
			String email,
			YearMonth targetMonth) {

		Long userId = attendanceMapper.findUserIdByEmail(email);

		if (userId == null) {
			throw new IllegalArgumentException(
					"ログインユーザーが見つかりません。");
		}

		String yyyymm = targetMonth.format(
				DateTimeFormatter.ofPattern("yyyyMM"));

		Long attendanceHeadId = attendanceMapper.findAttendanceHeadId(userId, yyyymm);

		// 未保存の場合はステータスなし
		if (attendanceHeadId == null) {
			return null;
		}

		return attendanceMapper.findAttendanceHeadStatus(
				attendanceHeadId);
	}

	/**
	 * 差戻中の勤怠情報をすべて取得する
	 */
	@Override
	public List<RejectedAttendanceDetail> getRejectedAttendanceList(
			String email) {

		Long userId = attendanceMapper.findUserIdByEmail(email);

		if (userId == null) {
			throw new IllegalArgumentException(
					"ログインユーザーが見つかりません。");
		}

		return attendanceMapper.findRejectedAttendances(userId);
	}

	/**
	 * 曜日を日本語へ変換する
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

	/**
	 * 勤怠区分コードを名称へ変換する
	 */
	private String getAttendanceType(String kbn) {

		return switch (kbn) {
		case "1" -> "出勤";
		case "2" -> "休日";
		case "3" -> "有給";
		case "4" -> "休出";
		case "5" -> "欠勤";
		case "6" -> "特休";
		case "7" -> "代休";
		case "8" -> "振休";
		default -> "";
		};
	}
}
package com.example.demo.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.model.AttendanceManage;
import com.example.demo.form.AttendanceManageSearchForm;
import com.example.demo.repository.AttendanceMapper;

/**
 * 勤怠管理サービス実装
 */
@Service
@Transactional
public class AttendanceManageServiceImpl
		implements AttendanceManageService {

	@Autowired
	private AttendanceMapper attendanceMapper;

	//勤怠一覧を取得

	@Override
	public List<AttendanceManage> getAttendanceManageList(
			AttendanceManageSearchForm searchForm) {

		return attendanceMapper.findAttendanceManageList(
				searchForm);
	}

	//勤怠一覧の件数を取得

	@Override
	public int getAttendanceManageCount(
			AttendanceManageSearchForm searchForm) {

		return attendanceMapper.countAttendanceManageList(
				searchForm);
	}

	//選択された勤怠を承認する
	@Override
    public void approveAttendances(
            List<Long> attendanceHeadIds) {

        // 勤怠未選択
        if (attendanceHeadIds == null
                || attendanceHeadIds.isEmpty()) {

            throw new IllegalArgumentException(
                    "勤怠が選択されていません。");
        }

        // 申請中以外の勤怠が含まれているか確認
        int notPendingCount =
                attendanceMapper.countNotPendingAttendances(
                        attendanceHeadIds);

        if (notPendingCount > 0) {

            throw new IllegalStateException(
                    "申請中以外の勤怠は承認できません。");
        }

        // ステータスを承認済へ更新
        int updatedCount =
                attendanceMapper.approveAttendances(
                        attendanceHeadIds);

        // 選択件数と更新件数が一致しない場合
        if (updatedCount != attendanceHeadIds.size()) {

            throw new IllegalStateException(
                    "勤怠の承認処理に失敗しました。");
        }
	}

	/**
	 * 選択された勤怠を差戻する
	 */
	@Override
	public void rejectAttendances(
			List<Long> attendanceHeadIds,
			String rejectReason) {

		// 勤怠未選択
		if (attendanceHeadIds == null
				|| attendanceHeadIds.isEmpty()) {

			throw new IllegalArgumentException(
					"勤怠が選択されていません。");
		}

		// 差戻理由未入力
		if (rejectReason == null
				|| rejectReason.isBlank()) {

			throw new IllegalArgumentException(
					"差戻理由を入力してください。");
		}

		// 申請中以外の勤怠が含まれているか確認
		int notPendingCount = attendanceMapper.countNotPendingAttendances(
				attendanceHeadIds);

		if (notPendingCount > 0) {

			throw new IllegalStateException(
					"申請中以外の勤怠は差戻できません。");
		}

		// 差戻中へ更新
		int updatedCount = attendanceMapper.rejectAttendances(
				attendanceHeadIds,
				rejectReason.trim());

		// 選択件数と更新件数が一致しない場合
		if (updatedCount != attendanceHeadIds.size()) {

			throw new IllegalStateException(
					"勤怠の差戻処理に失敗しました。");
		}

	}
	
}
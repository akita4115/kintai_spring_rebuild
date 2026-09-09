package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.model.AttendanceManage;
import com.example.demo.domain.service.AttendanceManageService;
import com.example.demo.form.AttendanceManageSearchForm;

/**
 * 勤怠管理画面Controller
 */
@Controller
@RequestMapping("/attendance/manage")
public class AttendanceManageController {

	@Autowired
	private AttendanceManageService attendanceManageService;

	/**
	 * 勤怠管理画面を表示
	 */
	@GetMapping
	public String getAttendanceManage(
			@ModelAttribute AttendanceManageSearchForm attendanceManageSearchForm,
			Model model) {

		// 不正なページ番号を補正
		if (attendanceManageSearchForm.getPage() < 1) {
			attendanceManageSearchForm.setPage(1);
		}

		// 検索条件に該当する勤怠一覧を取得
		List<AttendanceManage> attendanceManageList = attendanceManageService.getAttendanceManageList(
				attendanceManageSearchForm);

		// 検索結果の総件数を取得
		int totalCount = attendanceManageService.getAttendanceManageCount(
				attendanceManageSearchForm);

		// 総ページ数を計算
		int totalPages = (int) Math.ceil(
				(double) totalCount
						/ attendanceManageSearchForm.getPageSize());

		// 検索結果を画面へ渡す
		model.addAttribute(
				"attendanceManageList",
				attendanceManageList);

		// 検索結果件数を画面へ渡す
		model.addAttribute(
				"totalCount",
				totalCount);

		// 総ページ数を画面へ渡す
		model.addAttribute(
				"totalPages",
				totalPages);

		// メニューの勤怠管理をアクティブにする
		model.addAttribute(
				"activePage",
				"attendanceManage");

		return "attendance/manage";
	}

	/**
	 * 選択された勤怠を承認
	 */
	@PostMapping("/approve")
	public String postApprove(
			@RequestParam(required = false) List<Long> attendanceHeadIds,
			@RequestParam(required = false) String targetMonth,
			@RequestParam(required = false) String employeeCode,
			@RequestParam(required = false) String employeeName,
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "1") int page,
			RedirectAttributes redirectAttributes) {

		try {

			// 承認処理
			attendanceManageService.approveAttendances(
					attendanceHeadIds);

			// 承認完了メッセージ
			redirectAttributes.addFlashAttribute(
					"successMessage",
					"承認処理が完了しました。");

		} catch (IllegalArgumentException
				| IllegalStateException ex) {

			// 承認失敗メッセージ
			redirectAttributes.addFlashAttribute(
					"errorMessage",
					ex.getMessage());
		}

		setSearchCondition(
				redirectAttributes,
				targetMonth,
				employeeCode,
				employeeName,
				status,
				page);

		return "redirect:/attendance/manage";
	}

	/**
	 * 選択された勤怠を差戻
	 */
	@PostMapping("/reject")
	public String postReject(
			@RequestParam(required = false) List<Long> attendanceHeadIds,
			@RequestParam(required = false) String rejectReason,
			@RequestParam(required = false) String targetMonth,
			@RequestParam(required = false) String employeeCode,
			@RequestParam(required = false) String employeeName,
			@RequestParam(required = false) String status,
			@RequestParam(defaultValue = "1") int page,
			RedirectAttributes redirectAttributes) {

		try {

			attendanceManageService.rejectAttendances(
					attendanceHeadIds,
					rejectReason);

			redirectAttributes.addFlashAttribute(
					"successMessage",
					"差戻処理が完了しました。");

		} catch (IllegalArgumentException
				| IllegalStateException ex) {

			redirectAttributes.addFlashAttribute(
					"errorMessage",
					ex.getMessage());
		}

		setSearchCondition(
				redirectAttributes,
				targetMonth,
				employeeCode,
				employeeName,
				status,
				page);

		return "redirect:/attendance/manage";
	}

	/**
	 * 処理前の検索条件を引き継ぐ
	 */
	private void setSearchCondition(
			RedirectAttributes redirectAttributes,
			String targetMonth,
			String employeeCode,
			String employeeName,
			String status,
			int page) {

		redirectAttributes.addAttribute(
				"targetMonth",
				targetMonth);

		redirectAttributes.addAttribute(
				"employeeCode",
				employeeCode);

		redirectAttributes.addAttribute(
				"employeeName",
				employeeName);

		redirectAttributes.addAttribute(
				"status",
				status);

		redirectAttributes.addAttribute(
				"page",
				page);
	}
}
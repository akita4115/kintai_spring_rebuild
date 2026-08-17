package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.model.Holiday;
import com.example.demo.domain.service.HolidayService;
import com.example.demo.form.HolidayDetailForm;
import com.example.demo.form.HolidaySearchForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/holiday")
public class HolidayController {

	@Autowired
	private HolidayService holidayService;

	/**
	 * 祝日マスタ一覧画面
	 */
	@GetMapping("/index")
	public String getIndex(
			@ModelAttribute HolidaySearchForm holidaySearchForm,
			Model model) {

		// 検索条件で祝日一覧を取得
		List<Holiday> holidayList = holidayService.getHolidayList(holidaySearchForm);

		// 検索結果の件数を取得
		int totalCount = holidayService.getHolidayCount(holidaySearchForm);
		
		//総ページ数を計算
		int totalPages = (int) Math.ceil((double) totalCount / holidaySearchForm.getPageSize());
		
		
		

		// 取得した祝日一覧を画面へ渡す
		model.addAttribute("holidayList", holidayList);

		// 検索結果件数を画面へ渡す
		model.addAttribute("totalCount", totalCount);
		
		//総ページ数を画面へ渡す
		model.addAttribute("totalPages", totalPages);

		// 検索条件を画面へ渡す
		model.addAttribute("holidaySearchForm", holidaySearchForm);
		
		// メニューバーの祝日マスタ管理をアクティブ表示
		model.addAttribute("activePage", "holiday");

		// 新規登録フォーム
		if (!model.containsAttribute("holidayDetailForm")) {

			model.addAttribute(
					"holidayDetailForm",
					new HolidayDetailForm());
		}

		return "holiday/index";
	}

	/**
	 * 祝日新規登録
	 */
	@PostMapping("/create")
	public String postCreate(
			@ModelAttribute @Valid HolidayDetailForm holidayDetailForm,
			BindingResult bindingResult,
			@RequestParam(required = false) String searchYyyymmdd,
			@RequestParam(required = false) String searchHolidayName,
			Model model,
			RedirectAttributes redirectAttributes) {

		// 入力チェックエラー
		if (bindingResult.hasErrors()) {

			HolidaySearchForm holidaySearchForm = new HolidaySearchForm();

			holidaySearchForm.setYyyymmdd(searchYyyymmdd);
			holidaySearchForm.setHolidayName(searchHolidayName);

			// 検索条件で一覧を再取得
			List<Holiday> holidayList = holidayService.getHolidayList(holidaySearchForm);

			int totalCount = holidayService.getHolidayCount(holidaySearchForm);

			model.addAttribute(
					"holidayList",
					holidayList);

			model.addAttribute(
					"totalCount",
					totalCount);

			model.addAttribute(
					"holidaySearchForm",
					holidaySearchForm);

			model.addAttribute(
					"openCreateModal",
					true);

			return "holiday/index";
		}

		// FormからHolidayへ詰め替え
		Holiday holiday = new Holiday();

		holiday.setYyyymmdd(
				LocalDate.parse(
						holidayDetailForm.getYyyymmdd()));

		holiday.setHolidayName(
				holidayDetailForm.getHolidayName());

		// 登録
		holidayService.insertHoliday(holiday);

		// 登録前の検索条件を引き継ぐ
		setSearchCondition(
				redirectAttributes,
				searchYyyymmdd,
				searchHolidayName);

		return "redirect:/holiday/index";
	}

	/**
	 * 祝日更新
	 */
	@PostMapping("/update")
	public String postUpdate(
			@RequestParam Long id,
			@RequestParam String yyyymmdd,
			@RequestParam String holidayName,
			@RequestParam(required = false) String searchYyyymmdd,
			@RequestParam(required = false) String searchHolidayName,
			RedirectAttributes redirectAttributes) {

		// 更新対象を作成
		Holiday holiday = new Holiday();

		holiday.setId(id);

		holiday.setYyyymmdd(
				LocalDate.parse(yyyymmdd));

		holiday.setHolidayName(
				holidayName);

		// 更新
		holidayService.updateHoliday(holiday);

		// 更新前の検索条件を引き継ぐ
		setSearchCondition(
				redirectAttributes,
				searchYyyymmdd,
				searchHolidayName);

		return "redirect:/holiday/index";
	}

	/**
	 * 祝日削除
	 */
	@PostMapping("/delete")
	public String postDelete(
			@RequestParam Long id,
			@RequestParam(required = false) String searchYyyymmdd,
			@RequestParam(required = false) String searchHolidayName,
			RedirectAttributes redirectAttributes) {

		// 削除
		holidayService.deleteHoliday(id);

		// 削除前の検索条件を引き継ぐ
		setSearchCondition(
				redirectAttributes,
				searchYyyymmdd,
				searchHolidayName);

		return "redirect:/holiday/index";
	}

	/**
	 * 検索条件をリダイレクト先へ引き継ぐ
	 */
	private void setSearchCondition(
			RedirectAttributes redirectAttributes,
			String searchYyyymmdd,
			String searchHolidayName) {

		if (searchYyyymmdd != null
				&& !searchYyyymmdd.isBlank()) {

			redirectAttributes.addAttribute(
					"yyyymmdd",
					searchYyyymmdd);
		}

		if (searchHolidayName != null
				&& !searchHolidayName.isBlank()) {

			redirectAttributes.addAttribute(
					"holidayName",
					searchHolidayName);
		}

		// 更新・登録後は1ページ目から再検索
		redirectAttributes.addAttribute(
				"page",
				1);
	}
}
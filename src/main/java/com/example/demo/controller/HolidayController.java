package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.model.Holiday;
import com.example.demo.domain.service.HolidayService;
import com.example.demo.form.HolidaySearchForm;

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
		//検索条件で祝日一覧を取得
		List<Holiday> holidayList = holidayService.getHolidayList(holidaySearchForm);

		// 検索結果の件数を取得
		int totalCount = holidayService.getHolidayCount(holidaySearchForm);

		//取得した祝日一覧を画面へ渡す
		model.addAttribute("holidayList", holidayList);

		// 検索結果件数を画面へ渡す
		model.addAttribute("totalCount", totalCount);

		//検索条件を画面へ渡す
		model.addAttribute("holidaySearchForm", holidaySearchForm);

		return "holiday/index";
	}
}

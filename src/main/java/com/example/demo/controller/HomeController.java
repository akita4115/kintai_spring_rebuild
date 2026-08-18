package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	/**
	 * トップページ
	 */
	@GetMapping("/")
	public String getIndex() {

		// 勤怠入力画面へ遷移
		return "redirect:/attendance/input";
	}


	/**
	 * 勤怠入力画面
	 */
	@GetMapping("/attendance/input")
	public String getAttendanceInput(Model model) {
		
		//勤怠入力メニューをアクティブに
		model.addAttribute("activePage", "attendanceInput");

		return "attendance/input";
	}


	/**
	 * 勤怠管理画面
	 */
	@GetMapping("/attendance/manage")
	public String getAttendanceManage(Model model) {
		
		//勤怠管理メニューをアクティブに
				model.addAttribute("activePage", "attendanceManage");

		return "attendance/manage";
	}
}
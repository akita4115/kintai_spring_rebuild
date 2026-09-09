package com.example.demo.controller;

import org.springframework.stereotype.Controller;
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

}
package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

	/**
	 * 社員マスタ管理画面を表示
	 */
	@GetMapping("/index")
	public String getIndex(Model model) {

		log.info("社員マスタ管理画面：開始");

		// メニューバーの社員マスタ管理をアクティブ表示
		model.addAttribute("activePage","user");

		return "user/index";
	}
}
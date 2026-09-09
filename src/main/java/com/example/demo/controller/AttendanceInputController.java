package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 勤怠入力画面Controller
 */
@Controller
@RequestMapping("/attendance/input")
public class AttendanceInputController {

    /**
     * 勤怠入力画面を表示
     */
    @GetMapping
    public String getAttendanceInput(
            Model model) {

        // 勤怠入力メニューをアクティブにする
        model.addAttribute(
                "activePage",
                "attendanceInput");

        return "attendance/input";
    }
}
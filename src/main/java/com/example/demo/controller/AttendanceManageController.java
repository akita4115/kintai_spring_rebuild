package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
            @ModelAttribute
            AttendanceManageSearchForm attendanceManageSearchForm,
            Model model) {

        // 不正なページ番号を補正
        if (attendanceManageSearchForm.getPage() < 1) {
            attendanceManageSearchForm.setPage(1);
        }

        // 検索条件に該当する勤怠一覧を取得
        List<AttendanceManage> attendanceManageList =
                attendanceManageService.getAttendanceManageList(
                        attendanceManageSearchForm);

        // 検索結果の総件数を取得
        int totalCount =
                attendanceManageService.getAttendanceManageCount(
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
}
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
}
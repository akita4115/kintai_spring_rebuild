package com.example.demo.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.Holiday;
import com.example.demo.form.HolidaySearchForm;
import com.example.demo.repository.HolidayMapper;

@Service
public class HolidayService {

	@Autowired
	private HolidayMapper holidayMapper;
	
	//一覧検索
	public List<Holiday> getHolidayList(HolidaySearchForm form) {
		return holidayMapper.findMany(form);
		
	}
	
	//件数取得
	public int getHolidayCount(HolidaySearchForm form) {
		return holidayMapper.count(form);
		
	}
	
	//1件取得
	public Holiday getHoliday(Long id) {
		return holidayMapper.findOne(id);
		
	}
	
	//登録
	public void insertHoliday(Holiday holiday) {
		holidayMapper.insert(holiday);
	}
	
	//更新
	public void updateHoliday(Holiday holiday) {
		holidayMapper.update(holiday);
	}
	
	//削除
	public void deleteHoliday(Long id) {
		holidayMapper.delete(id);
	}
}
	
	

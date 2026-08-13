package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.domain.model.Holiday;
import com.example.demo.form.HolidaySearchForm;

@Mapper
public interface HolidayMapper {

	//祝日一覧取得
	List<Holiday> findMany(HolidaySearchForm from);
	
	//検索件数取得（ページング用）
	int count(HolidaySearchForm form);
	
	//IDで一件取得
	Holiday findOne(Long id);
	
	//登録
	void insert(Holiday holiday);
	
	//更新
	void update(Holiday holiday);
	
	//削除
	void delete(Long id);
	
}

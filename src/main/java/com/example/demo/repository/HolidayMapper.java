package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.model.Holiday;
import com.example.demo.form.HolidaySearchForm;

@Mapper
public interface HolidayMapper {

	// 祝日一覧取得
	List<Holiday> findMany(HolidaySearchForm form);

	// 検索件数取得（ページング用）
	int count(HolidaySearchForm form);

	// IDで1件取得
	Holiday findOne(Long id);

	// 指定期間の祝日一覧取得
	List<Holiday> findByPeriod(
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	// 登録
	void insert(Holiday holiday);

	// 更新
	void update(Holiday holiday);

	// 削除
	void delete(Long id);
}
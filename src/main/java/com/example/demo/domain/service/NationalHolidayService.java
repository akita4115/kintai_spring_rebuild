package com.example.demo.domain.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import lombok.extern.slf4j.Slf4j;

/**
 * 国民の祝日取得サービス
 */
@Service
@Slf4j
public class NationalHolidayService {

	private static final String API_URL = "https://holidays-jp.github.io/api/v1/{year}/date.json";

	private final RestClient restClient = RestClient.create();

	/**
	 * 対象年月の国民の祝日を取得する
	 */
	public Map<LocalDate, String> getNationalHolidays(
			YearMonth targetMonth) {

		try {
			Map<String, String> response = restClient.get()
					.uri(
							API_URL,
							targetMonth.getYear())
					.retrieve()
					.body(
							new ParameterizedTypeReference<Map<String, String>>() {
							});

			Map<LocalDate, String> holidayMap = new HashMap<>();

			if (response == null) {
				return holidayMap;
			}

			for (Map.Entry<String, String> entry : response.entrySet()) {

				LocalDate date = LocalDate.parse(entry.getKey());

				if (YearMonth.from(date)
						.equals(targetMonth)) {

					holidayMap.put(
							date,
							entry.getValue());
				}
			}

			return holidayMap;

		} catch (RestClientException ex) {
			log.error(
					"国民の祝日の取得に失敗しました。",
					ex);

			return new HashMap<>();
		}
	}
}
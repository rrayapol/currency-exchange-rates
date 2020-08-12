package com.exchange.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.exchange.dao.CurrencyExchangePersister;
import com.exchange.model.ExchangeRequest;
import com.exchange.model.ExchangeResponse;

@Service
public class CurrencyExchangeService {

	@Autowired
	DefaultCurrencyExchangeService defaultCurrencyExchangeService;

	@Autowired
	AsOfDateCurrencyExchangeService asOfDateCurrencyExchangeService;

	@Autowired
	YearCurrencyExchangeService yearCurrencyExchangeService;

	@Autowired
	CurrencyExchangePersister currencyExchangePersister;

	public List<ExchangeResponse> processPostExchange(ExchangeRequest request) {
		List<ExchangeResponse> responseList = new ArrayList<>();

		if (!StringUtils.isEmpty(request.getAsOfDate())) {
			responseList = asOfDateCurrencyExchangeService.process(request);
			currencyExchangePersister.save(responseList);
		} else if (!StringUtils.isEmpty(request.getYear())) {
			responseList = yearCurrencyExchangeService.process(request);
		} else {
			responseList = defaultCurrencyExchangeService.process(request);
			currencyExchangePersister.save(responseList);
		}

		return responseList;

	}

	public List<ExchangeResponse> processGetMapping(String asOfDate, String startDate, String endDate) {
		List<ExchangeResponse> dbResponseList = new ArrayList<>();
		if (!StringUtils.isEmpty(asOfDate)) {
			dbResponseList = currencyExchangePersister.getExchangeDetails(asOfDate);
		} else if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
			dbResponseList = currencyExchangePersister.getExchangeDetails(startDate, endDate);
		}
		return dbResponseList;
	}

}

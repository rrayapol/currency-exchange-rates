package com.exchange.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.exchange.dao.CurrencyExchangePersister;
import com.exchange.exception.CurrencyExchangeException;
import com.exchange.factory.CurrencyExchangeFactory;
import com.exchange.model.ExchangeRequest;
import com.exchange.model.ExchangeResponse;
import com.exchange.service.CurrencyExchangeService;
import com.exchange.util.Constants;

@Service
public class CurrencyExchangeProcessor {

	Logger logger = LoggerFactory.getLogger(CurrencyExchangePersister.class);

	@Resource(name = "currencyExchangeFactory")
	CurrencyExchangeFactory currencyExchangeFactory;

	@Autowired
	CurrencyExchangePersister currencyExchangePersister;

	public List<ExchangeResponse> processPostExchange(ExchangeRequest request) {
		try {
			logger.info("Post Exchange process started {}", request);
			List<ExchangeResponse> responseList = new ArrayList<>();
			String serviceName = getServiceName(request);
			CurrencyExchangeService currencyExchangeService = currencyExchangeFactory
					.getService(serviceName + Constants.CURRENCY_EXCHANGE_SERVICE);
			responseList = currencyExchangeService.process(request);
			logger.info("Post Exchange process completed {}", request);
			return responseList;
		} catch (Exception ex) {
			logger.error("Error: CurrencyExchange Process Failed {}", ex);
			throw new CurrencyExchangeException("CurrencyExchange Process Failed");
		}
	}

	private String getServiceName(ExchangeRequest request) {
		String serviceName = "";
		if (!StringUtils.isEmpty(request.getAsOfDate())) {
			serviceName = Constants.ASOFDATE;
		} else if (!StringUtils.isEmpty(request.getYear())) {
			serviceName = Constants.YEAR;
		} else {
			serviceName = Constants.DEFAULT;
		}
		return serviceName;
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

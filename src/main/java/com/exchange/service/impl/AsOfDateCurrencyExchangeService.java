package com.exchange.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exchange.client.RatesApiClient;
import com.exchange.dao.CurrencyExchangePersister;
import com.exchange.exception.CurrencyExchangeException;
import com.exchange.model.ExchangeRequest;
import com.exchange.model.ExchangeResponse;
import com.exchange.service.CurrencyExchangeService;

@Service("asOfDateCurrencyExchangeService")
public class AsOfDateCurrencyExchangeService implements CurrencyExchangeService {

	Logger logger = LoggerFactory.getLogger(CurrencyExchangePersister.class);

	@Autowired
	RatesApiClient ratesApiClient;

	@Autowired
	CurrencyExchangePersister currencyExchangePersister;

	/**
	 * This method get the mapping details based on asOfDate
	 */
	@Override
	public List<ExchangeResponse> process(ExchangeRequest request) {
		try {
			logger.info("asOfDateCurrencyExchangeService process started {}", request);
			List<ExchangeResponse> exchResponses = new ArrayList<>();
			exchResponses.add(ratesApiClient.getCurrenyDetails(request.getAsOfDate()));
			currencyExchangePersister.save(exchResponses);
			logger.info("asOfDateCurrencyExchangeService process completed {}", request);
			return exchResponses;
		} catch (Exception ex) {
			logger.error("Error: asOfDateCurrencyExchangeService failed {}", ex);
			throw new CurrencyExchangeException("CurrencyExchange Process Failed");
		}
	}

}

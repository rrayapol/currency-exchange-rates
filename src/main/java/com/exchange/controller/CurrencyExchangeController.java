package com.exchange.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchange.dao.CurrencyExchangePersister;
import com.exchange.exception.CurrencyExchangeException;
import com.exchange.model.ExchangeRequest;
import com.exchange.model.ExchangeResponse;
import com.exchange.service.impl.CurrencyExchangeProcessor;

@RestController
@RequestMapping("/currency-exchange")
public class CurrencyExchangeController {

	Logger logger = LoggerFactory.getLogger(CurrencyExchangePersister.class);

	@Autowired
	CurrencyExchangeProcessor currencyExchangeProcessor;

	/**
	 * This method will invoke for POST requests
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping
	List<ExchangeResponse> postProcessCurrencyExchange(@RequestBody ExchangeRequest request) {
		try {
			logger.info("CurrencyExchange post Process Started {}", request);
			List<ExchangeResponse> response = currencyExchangeProcessor.processPostExchange(request);
			logger.info("CurrencyExchange post Process completed {}", request);
			return response;
		} catch (Exception ex) {
			logger.error("Error: Currency Exchange post Process Failed {} {}", ex, request);
			throw new CurrencyExchangeException("CurrencyExchange Process Failed");
		}
	}

	/**
	 * This method will invoke for GET requests.
	 * 
	 * @param asOfDate
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping
	List<ExchangeResponse> getProcessCurrencyExchange(
			@RequestParam(name = "asOfDate", required = false) String asOfDate,
			@RequestParam(name = "startDate", required = false) String startDate,
			@RequestParam(name = "endDate", required = false) String endDate) {
		try {
			logger.info("CurrencyExchange get Process Started {} {} {}", asOfDate, startDate, endDate);
			List<ExchangeResponse> response = currencyExchangeProcessor.processGetMapping(asOfDate, startDate, endDate);
			logger.info("CurrencyExchange get Process completed {} {} {}", asOfDate, startDate, endDate);
			return response;
		} catch (Exception ex) {
			logger.error("Error: Currency Exchange get Process Failed {} {} {} {}", ex, asOfDate, startDate, endDate);
			throw new CurrencyExchangeException("CurrencyExchange Process Failed");
		}
	}

}

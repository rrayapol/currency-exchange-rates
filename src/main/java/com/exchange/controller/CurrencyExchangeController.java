package com.exchange.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchange.model.ExchangeRequest;
import com.exchange.model.ExchangeResponse;
import com.exchange.service.CurrencyExchangeService;

@RestController
@RequestMapping("/currency-exchange")
public class CurrencyExchangeController {

	@Autowired
	CurrencyExchangeService currencyExchangeService;

	@PostMapping
	List<ExchangeResponse> postProcessCurrencyExchange(@RequestBody ExchangeRequest request){
		List<ExchangeResponse> response = currencyExchangeService.processPostExchange(request);
		return response;
	}
	
	@GetMapping
	List<ExchangeResponse> getProcessCurrencyExchange(@RequestParam(name = "asOfDate", required = false) String asOfDate, @RequestParam(name = "startDate", required = false) String startDate, @RequestParam(name = "endDate", required = false) String endDate){
		List<ExchangeResponse> response = currencyExchangeService.processGetMapping(asOfDate, startDate, endDate);
		return response;
	}

}

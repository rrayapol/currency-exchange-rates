package com.exchange.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exchange.client.RatesApiClient;
import com.exchange.model.ExchangeRequest;
import com.exchange.model.ExchangeResponse;

@Service
public class AsOfDateCurrencyExchangeService {

	@Autowired
	RatesApiClient ratesApiClient;


	List<ExchangeResponse> process(ExchangeRequest request) {
		List<ExchangeResponse> exchResponses = new ArrayList<>();
		exchResponses.add(ratesApiClient.getCurrenyDetails(request.getAsOfDate()));
		return exchResponses;
	}

}

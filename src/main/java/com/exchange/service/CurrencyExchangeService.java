package com.exchange.service;

import java.util.List;

import com.exchange.model.ExchangeRequest;
import com.exchange.model.ExchangeResponse;

public interface CurrencyExchangeService {

	List<ExchangeResponse> process(ExchangeRequest request);
}

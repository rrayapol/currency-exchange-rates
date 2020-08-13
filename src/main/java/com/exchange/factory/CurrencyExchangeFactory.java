package com.exchange.factory;

import com.exchange.service.CurrencyExchangeService;

public interface CurrencyExchangeFactory {

	  CurrencyExchangeService getService(String service);
	
}

package com.exchange.model;

import java.util.Map;

public class ExchangeResponse {

	private Map<String, Object> rates;
	private String date;
	private String asOfDate;
	
	
	public String getAsOfDate() {
		return asOfDate;
	}
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}
	public Map<String, Object> getRates() {
		return rates;
	}
	public void setRates(Map<String, Object> rates) {
		this.rates = rates;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}

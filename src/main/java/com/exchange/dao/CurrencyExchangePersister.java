package com.exchange.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.exchange.exception.CurrencyExchangeException;
import com.exchange.model.CurrencyExchangeRate;
import com.exchange.model.ExchangeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class CurrencyExchangePersister {

	@Autowired
	CurrencyExchangeRepository CurrencyExchangeRepository;

	public void save(List<ExchangeResponse> response) {
		List<CurrencyExchangeRate> exchangeList = prepareExchangeModel(response);
		for (CurrencyExchangeRate entity : exchangeList) {
			CurrencyExchangeRepository.save(entity);
		}
	}

	public void saveAll(List<ExchangeResponse> response) {
		CurrencyExchangeRate exchange = prepareFinalExchangeModel(response);
		CurrencyExchangeRepository.save(exchange);
	}

	private CurrencyExchangeRate prepareFinalExchangeModel(List<ExchangeResponse> response) {
		try {
			CurrencyExchangeRate ce = new CurrencyExchangeRate();
			ce.setDateOfDate(formatDate(response.get(0).getAsOfDate()));
			ce.setProcess_date(new Date());
			ce.setHasMonthData(1);
			ObjectMapper mapper = new ObjectMapper();
			String newJsonData = mapper.writeValueAsString(response);
			ce.setResponse(newJsonData);
			return ce;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new CurrencyExchangeException("DB Operation Failed");
		}
	}

	private List<CurrencyExchangeRate> prepareExchangeModel(List<ExchangeResponse> response) {
		List<CurrencyExchangeRate> exchangeList = new ArrayList<>();
		try {
			for (ExchangeResponse er : response) {
				CurrencyExchangeRate ce = new CurrencyExchangeRate();
				ce.setDateOfDate(formatDate(er.getAsOfDate()));
				ce.setProcess_date(new Date());
				ce.setHasMonthData(0);
				ObjectMapper mapper = new ObjectMapper();
				String newJsonData = mapper.writeValueAsString(er);
				ce.setResponse(newJsonData);
				exchangeList.add(ce);
			}
			return exchangeList;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new CurrencyExchangeException("DB Operation Failed");
		}
	}

	private static Date formatDate(String date) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(date);
		} catch (Exception ex) {
			throw new CurrencyExchangeException("Error");
		}
	}

	public List<ExchangeResponse> getExchangeDetails(String asOfDate) {
		List<CurrencyExchangeRate> dbResponse = CurrencyExchangeRepository
				.findByDataOfDateAndHasMonthData(formatDate(asOfDate), 0);
		List<ExchangeResponse> response = prepareCurrencyExchange(dbResponse);
		return response;
	}

	private List<ExchangeResponse> prepareCurrencyExchange(List<CurrencyExchangeRate> dbResponse) {
		List<ExchangeResponse> exchangelist = new ArrayList<>();
		try {
			for (CurrencyExchangeRate cer : dbResponse) {
				ObjectMapper om = new ObjectMapper();
				ExchangeResponse er = om.readValue(cer.getResponse(), ExchangeResponse.class);
				exchangelist.add(er);
			}
			return exchangelist;
		} catch (Exception e) {
			e.printStackTrace();
			throw new CurrencyExchangeException("Failed");
		}
	}

	public List<ExchangeResponse> getExchangeDetails(String startDate, String endDate) {

		List<CurrencyExchangeRate> dbResponse = CurrencyExchangeRepository
				.getAllBetweenDatesAndHasMonthData(formatDate(startDate), formatDate(endDate), 0);
		List<ExchangeResponse> response = prepareCurrencyExchange(dbResponse);
		return response;
	}

}

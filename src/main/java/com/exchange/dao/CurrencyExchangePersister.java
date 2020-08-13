package com.exchange.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.exchange.exception.CurrencyExchangeException;
import com.exchange.model.CurrencyExchangeRate;
import com.exchange.model.ExchangeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class CurrencyExchangePersister {

	Logger logger = LoggerFactory.getLogger(CurrencyExchangePersister.class);

	@Autowired
	CurrencyExchangeRepository CurrencyExchangeRepository;

	/**
	 * This method is responsible for save data as per given date
	 * 
	 * @param response
	 */
	public void save(List<ExchangeResponse> response) {
		try {
			logger.info("saving into DB");
			List<CurrencyExchangeRate> exchangeList = prepareExchangeModel(response);
			for (CurrencyExchangeRate entity : exchangeList) {
				CurrencyExchangeRepository.save(entity);
			}
			logger.info("successfully saved into DB");
		} catch (Exception ex) {
			logger.error("DB Operation Failed {}", ex);
			throw new CurrencyExchangeException("DB Operation Failed");
		}
	}

	/**
	 * This method is responsible for save data as per Month
	 * 
	 * @param response
	 */
	public void saveAll(List<ExchangeResponse> response) {
		try {
			logger.info("saving all into DB");
			CurrencyExchangeRate exchange = prepareFinalExchangeModel(response);
			CurrencyExchangeRepository.save(exchange);
			logger.info("successfully saved all into DB");
		} catch (Exception ex) {
			logger.error("DB Operation Failed {}", ex);
			throw new CurrencyExchangeException("DB OPeration Failed");
		}
	}

	public List<ExchangeResponse> getExchangeDetails(String asOfDate) {
		try {
			logger.info("fetching ExchangeDetails from DB {}", asOfDate);
			List<CurrencyExchangeRate> dbResponse = CurrencyExchangeRepository
					.findByDataOfDateAndHasMonthData(formatDate(asOfDate), 0);
			List<ExchangeResponse> response = prepareCurrencyExchange(dbResponse);
			logger.info("successfully fetched ExchangeDetails from DB {}", asOfDate);
			return response;
		} catch (Exception ex) {
			logger.error("DB Operation Failed {}", ex);
			throw new CurrencyExchangeException("DB Operation failed");
		}
	}

	public List<ExchangeResponse> getExchangeDetails(String startDate, String endDate) {
		try {
			logger.info("fetching ExchangeDetails from DB {} {}", startDate, endDate);
			List<CurrencyExchangeRate> dbResponse = CurrencyExchangeRepository
					.getAllBetweenDatesAndHasMonthData(formatDate(startDate), formatDate(endDate), 0);
			List<ExchangeResponse> response = prepareCurrencyExchange(dbResponse);
			logger.info("successfully fetched ExchangeDetails from DB {} {}", startDate, endDate);
			return response;
		} catch (Exception ex) {
			logger.error("DB Operation Failed {}", ex);
			throw new CurrencyExchangeException("DB Operation failed");
		}
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
			throw new CurrencyExchangeException("DB Operation Failed");
		}
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
			throw new CurrencyExchangeException("DB Operation Failed");
		}
	}

}

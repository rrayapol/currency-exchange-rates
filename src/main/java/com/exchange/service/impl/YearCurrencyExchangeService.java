package com.exchange.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

@Service("yearCurrencyExchangeService")
public class YearCurrencyExchangeService implements CurrencyExchangeService {

	Logger logger = LoggerFactory.getLogger(CurrencyExchangePersister.class);

	@Autowired
	RatesApiClient ratesApiClient;

	@Autowired
	CurrencyExchangePersister currencyExchangePersister;

	/**
	 * This method get mappings for complete year as provided by user
	 */
	@Override
	public List<ExchangeResponse> process(ExchangeRequest request) {
		try {
			logger.info("YearCurrencyExchangeService started {}", request);
			List<ExchangeResponse> responseList = new ArrayList<>();
			Date startDate = getStartDate(request.getYear());
			Date endDate = getEndDate(startDate);
			while ((startDate.compareTo(endDate) < 0) && (startDate.compareTo(new Date()) < 0)) {
				List<ExchangeResponse> monthlyExchangeResponse = new ArrayList<>();
				Date nextMonthDate = getNextMonthDate(startDate);
				while (startDate.compareTo(nextMonthDate) < 0 && (startDate.compareTo(new Date()) < 0)) {
					String dateAsString = getDateAsString(startDate);
					monthlyExchangeResponse.add(ratesApiClient.getCurrenyDetails(dateAsString));
					startDate = getNextDayDate(startDate);
				}
				currencyExchangePersister.saveAll(monthlyExchangeResponse);
				responseList.addAll(monthlyExchangeResponse);
			}
			logger.info("YearCurrencyExchangeService completed {}", request);
			return responseList;
		} catch (Exception ex) {
			logger.error("YearCurrencyExchangeService failed {}", ex);
			throw new CurrencyExchangeException("Currecny Exchange Process Failed");
		}
	}

	private static Date getStartDate(String year) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.parseInt(year));
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DATE, 1);
			Date formatDate = cal.getTime();
			return formatDate;
		} catch (Exception ex) {
			throw new CurrencyExchangeException("Error");
		}
	}

	private static Date getEndDate(Date startDate) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.YEAR, 1);
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.DATE, 1);
			Date formatDate = cal.getTime();
			return formatDate;
		} catch (Exception ex) {
			throw new CurrencyExchangeException("Error");
		}
	}

	private static Date getNextMonthDate(Date date) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, 1);
			cal.set(Calendar.DATE, 1);
			Date formatDate = cal.getTime();
			return formatDate;
		} catch (Exception ex) {
			throw new CurrencyExchangeException("Error");
		}
	}

	private static Date getNextDayDate(Date date) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, 1);
			Date formatDate = cal.getTime();
			return formatDate;
		} catch (Exception ex) {
			throw new CurrencyExchangeException("Error");
		}
	}

	private static String getDateAsString(Date date) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(date);
		} catch (Exception ex) {
			throw new CurrencyExchangeException("Error");
		}
	}

}

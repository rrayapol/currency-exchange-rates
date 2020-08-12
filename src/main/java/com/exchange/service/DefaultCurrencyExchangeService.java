package com.exchange.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.exchange.client.RatesApiClient;
import com.exchange.exception.CurrencyExchangeException;
import com.exchange.model.ExchangeRequest;
import com.exchange.model.ExchangeResponse;

@Service
public class DefaultCurrencyExchangeService {

	@Autowired
	RatesApiClient ratesApiClient;
	

	List<ExchangeResponse> process(ExchangeRequest request) {

		List<ExchangeResponse> responseList = new ArrayList<>();
		Date startDate = getPrevious12MonthDate(new Date());
		Date endDate = getEndDate();
		while (startDate.compareTo(endDate) < 0 || startDate.compareTo(endDate) == 0 ) {
			String dateAsString = getDateAsString(startDate);
			responseList.add(ratesApiClient.getCurrenyDetails(dateAsString));
			startDate = getNextMonthDate(startDate);
		}

		return responseList;
	}

	private static Date getPrevious12MonthDate(Date date) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, -1);
			Date pre12MonthDate = cal.getTime();
			return pre12MonthDate;
		} catch (Exception ex) {
			throw new CurrencyExchangeException("Error");
		}
	}
	
	
	private static Date getEndDate() {
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -1);
			cal.set(Calendar.DATE, 1);
			Date pre12MonthDate = cal.getTime();
			return pre12MonthDate;
		} catch (Exception ex) {
			throw new CurrencyExchangeException("Error");
		}
	}

	private static String getDateAsString(Date date) {
		try {
			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DATE, 1);
			Date formatDate = cal.getTime();
			return sdf.format(formatDate);
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
			Date nextMonthDate = cal.getTime();
			return nextMonthDate;
		} catch (Exception ex) {
			throw new CurrencyExchangeException("Error");
		}
	}
}

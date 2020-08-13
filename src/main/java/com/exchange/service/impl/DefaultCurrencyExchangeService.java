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

@Service("defaultCurrencyExchangeService")
public class DefaultCurrencyExchangeService implements CurrencyExchangeService {

	static Logger logger = LoggerFactory.getLogger(CurrencyExchangePersister.class);

	@Autowired
	RatesApiClient ratesApiClient;

	@Autowired
	CurrencyExchangePersister currencyExchangePersister;

	/**
	 * This method get the mappings of last 12 months and consider only 1st day of
	 * month
	 */
	@Override
	public List<ExchangeResponse> process(ExchangeRequest request) {
		try {
			logger.info("defaultCurrencyExchangeService process started {}", request);
			List<ExchangeResponse> responseList = new ArrayList<>();
			Date startDate = getPrevious12MonthDate(new Date());
			Date endDate = getEndDate();
			while (startDate.compareTo(endDate) < 0 || startDate.compareTo(endDate) == 0) {
				String dateAsString = getDateAsString(startDate);
				responseList.add(ratesApiClient.getCurrenyDetails(dateAsString));
				startDate = getNextMonthDate(startDate);
			}
			currencyExchangePersister.save(responseList);
			logger.info("defaultCurrencyExchangeService process completed {}", request);
			return responseList;
		} catch (Exception ex) {
			logger.error("defaultCurrencyExchangeService process failed {}", request);
			throw new CurrencyExchangeException("Currency Exchange Process Failed");
		}
	}

	private static Date getPrevious12MonthDate(Date date) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.YEAR, -1);
			Date pre12MonthDate = cal.getTime();
			return pre12MonthDate;
		} catch (Exception ex) {
			logger.error("getPrevious12MonthDate  failed {}", ex);
			throw new CurrencyExchangeException("Currency Exchange Process Failed");
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
			logger.error("getEndDate  failed {}", ex);
			throw new CurrencyExchangeException("Currency Exchange Process Failed");
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
			logger.error("getDateAsString  failed {}", ex);
			throw new CurrencyExchangeException("Currency Exchange Process Failed");
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
			logger.error("getNextMonthDate  failed {}", ex);
			throw new CurrencyExchangeException("Currency Exchange Process Failed");
		}
	}
}

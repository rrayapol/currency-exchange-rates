package com.exchange.client;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.exchange.exception.CurrencyExchangeException;
import com.exchange.model.ExchangeResponse;
import com.exchange.util.Constants;

@Service
public class RatesApiClient {

	Logger logger = LoggerFactory.getLogger(RatesApiClient.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${ratesApiUrl}")
	String ratesApiUrl;

	/**
	 * This method is used to hit RatesAPI and get corresponding mapping value.
	 * 
	 * @param asofDate
	 * @return
	 */
	public ExchangeResponse getCurrenyDetails(String asofDate) {

		try {
			logger.info("RatesApiClient process started {}", asofDate);
			Map<String, String> params = new HashMap<>();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add(Constants.USER_AGENT,
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
			HttpEntity<String> entity = new HttpEntity<String>(Constants.PARAMETERS, headers);
			params.put(Constants.ASOFDATE, asofDate);
			params.put(Constants.SYMBOLS, "GBP,USD,HKD");
			ResponseEntity<ExchangeResponse> responseEntity = restTemplate.exchange(ratesApiUrl, HttpMethod.GET, entity,
					ExchangeResponse.class, params);
			ExchangeResponse response = responseEntity.getBody();
			response.setAsOfDate(asofDate);
			logger.info("RatesApiClient process completed {}", asofDate);
			return response;
		} catch (Exception ex) {
			logger.error("RatesApiClient Request Failed {}", ex);
			throw new CurrencyExchangeException("Currency Exchange process Failed");
		}
	}

}

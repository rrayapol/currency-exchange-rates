package com.exchange.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

@Service
public class RatesApiClient {

	@Autowired
	RestTemplate restTemplate;

	@Value("${ratesApiUrl}")
	String ratesApiUrl;

	public ExchangeResponse  getCurrenyDetails(String asofDate) {

		try {
			Map<String, String> params = new HashMap<>();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
	        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
			params.put("asOfDate", asofDate);
			params.put("symbols", "GBP,USD,HKD");
			ResponseEntity<ExchangeResponse> responseEntity = restTemplate.exchange(ratesApiUrl, HttpMethod.GET, entity, ExchangeResponse.class, params);
			ExchangeResponse response = responseEntity.getBody();
			response.setAsOfDate(asofDate);
			return response;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CurrencyExchangeException("RatesApiClient Request Failed");
		}
	}

}

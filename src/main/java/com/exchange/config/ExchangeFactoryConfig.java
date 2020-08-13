package com.exchange.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.exchange.factory.CurrencyExchangeFactory;

@Configuration
public class ExchangeFactoryConfig {

	  @Bean("currencyExchangeFactory")
	  public FactoryBean<?> serviceLocatorFactoryBean() {
	    ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
	    factoryBean.setServiceLocatorInterface(CurrencyExchangeFactory.class);
	    return factoryBean;
	  }
	
}

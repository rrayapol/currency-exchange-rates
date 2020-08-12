package com.exchange.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.exchange.model.CurrencyExchangeRate;

public interface CurrencyExchangeRepository extends CrudRepository<CurrencyExchangeRate, Long> {

	List<CurrencyExchangeRate> findByDataOfDateAndHasMonthData(Date dateOfDate, int hasMonthData);

	@Query(value = "from CurrencyExchangeRate t where dataOfDate BETWEEN :startDate AND :endDate AND hasMonthData = :hasMonthData")
    List<CurrencyExchangeRate> getAllBetweenDatesAndHasMonthData(@Param("startDate")Date startDate,@Param("endDate")Date endDate,@Param("hasMonthData")int hasMonthData);
}

package com.exchange.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "CURRENCY_EXCHANGE_RATE")
public class CurrencyExchangeRate {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(name = "DATA_OF_DATE")
	private Date dataOfDate;
	@Lob
	private String response;
	@Column(name = "PROCESSED_DATE")
	private Date process_date;
	@Column(name = "HAS_MONTH_DATA")
	private int hasMonthData;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDateOfDate() {
		return dataOfDate;
	}
	public void setDateOfDate(Date dateOfDate) {
		this.dataOfDate = dateOfDate;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Date getProcess_date() {
		return process_date;
	}
	public void setProcess_date(Date process_date) {
		this.process_date = process_date;
	}
	public int getHasMonthData() {
		return hasMonthData;
	}
	public void setHasMonthData(int hasMonthData) {
		this.hasMonthData = hasMonthData;
	}
	

}

package com.moneytransfer.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Response {

	private long toAccountNumber;
	private long fromAccountNumber;
	private String responseMessage;

	public long getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(long toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public long getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(long fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

}

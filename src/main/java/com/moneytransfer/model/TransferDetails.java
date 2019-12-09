package com.moneytransfer.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TransferDetails {
	
	private int fromAccountNumber;
	private int toAccountNumber;
	private int amount;
	private String statusMessage;
	
	/**
	 * @return the fromAccountNumber
	 */
	public int getFromAccountNumber() {
		return fromAccountNumber;
	}
	/**
	 * @param fromAccountNumber the fromAccountNumber to set
	 */
	public void setFromAccountNumber(int fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}
	/**
	 * @return the toAccountNumber
	 */
	public int getToAccountNumber() {
		return toAccountNumber;
	}
	/**
	 * @param toAccountNumber the toAccountNumber to set
	 */
	public void setToAccountNumber(int toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TransferDetails [fromAccountNumber=" + fromAccountNumber + ", toAccountNumber=" + toAccountNumber
				+ ", amount=" + amount + "]";
	}

	
}

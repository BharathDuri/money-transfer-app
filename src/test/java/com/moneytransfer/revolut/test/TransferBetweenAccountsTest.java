package com.moneytransfer.revolut.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.moneytransfer.configuration.DataSourceConfiguration;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.ResponseMessage;
import com.moneytransfer.model.TransferDetails;
import com.moneytransfer.service.MoneyTransferService;

public class TransferBetweenAccountsTest {

	/**
	 * Runs before class to intialize and load H2 DB with preloaded values.
	 */
	@BeforeClass
	public static void setup() {
		DataSourceConfiguration.setupDataSource();

	}

	/**
	 * Test to check transfer service is working.
	 * 
	 * Test to pass if the transfer service works.
	 */
	@Test
	public void testBalanceTransferService() {

		TransferDetails transactiondetails = new TransferDetails();
		transactiondetails.setFromAccountNumber(101);
		transactiondetails.setToAccountNumber(102);
		transactiondetails.setAmount(10);
		MoneyTransferService transferService = new MoneyTransferService();
		ResponseMessage response = transferService.transfer(transactiondetails);
		assertEquals("Successfully transferred " + transactiondetails.getAmount() + " to account: " + 102,
				response.getResponseMessage());
	}

	/**
	 * Test to check sender account amount is updated after transfer.
	 * 
	 * Test to check if transfer amount is deducted.
	 */
	@Test
	public void testSenderAmountUpdated() {

		TransferDetails transactiondetails = new TransferDetails();
		transactiondetails.setFromAccountNumber(101);
		transactiondetails.setToAccountNumber(102);
		transactiondetails.setAmount(10);
		MoneyTransferService transferService = new MoneyTransferService();
		// Fetch preftransfer account Details for sender
		Account preTransferAccount = transferService.getAccountDetails(101);
		ResponseMessage response = transferService.transfer(transactiondetails);
		// Fetch postftransfer account Details for sender
		Account postTransferAccount = transferService.getAccountDetails(101);

		assertEquals(preTransferAccount.getBalance() - transactiondetails.getAmount(), postTransferAccount.getBalance(),
				0.0001);
	}

	/**
	 * Test to check Recipient account amount is updated after transfer and transfer
	 * amount added.
	 * 
	 */
	@Test
	public void testReceipientAmountUpdated() {

		TransferDetails transactiondetails = new TransferDetails();
		transactiondetails.setFromAccountNumber(101);
		transactiondetails.setToAccountNumber(102);
		transactiondetails.setAmount(10);
		MoneyTransferService transferService = new MoneyTransferService();
		// Fetch pretransfer account Details for sender
		Account preTransfer = transferService.getAccountDetails(102);
		ResponseMessage response = transferService.transfer(transactiondetails);
		// Fetch posttransfer account Details for sender
		Account postTransfer = transferService.getAccountDetails(102);

		assertEquals(preTransfer.getBalance() + transactiondetails.getAmount(), postTransfer.getBalance(), 0.0001);
	}

	/**
	 * Test to check if transfer amount > available balance Service should fail and
	 * return responseMessage object with message "Insufficient Funds"
	 */
	@Test
	public void testInsufficientBalance() {

		TransferDetails transactiondetails = new TransferDetails();
		transactiondetails.setFromAccountNumber(101);
		transactiondetails.setToAccountNumber(102);
		transactiondetails.setAmount(1000000);

		MoneyTransferService transferService = new MoneyTransferService();
		ResponseMessage response = transferService.transfer(transactiondetails);

		Account preTransfer = transferService.getAccountDetails(101);
        
		assertEquals("Insufficient Funds-Available balance: " + preTransfer.getBalance(),
				response.getResponseMessage());

	}
}

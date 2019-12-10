package com.moneytransfer.revolut.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.moneytransfer.configuration.DataSourceConfiguration;
import com.moneytransfer.dao.AccountDetailsDAO;
import com.moneytransfer.model.Account;
import com.moneytransfer.service.MoneyTransferService;

/**
 * @author bharathduri
 *
 *
 *Unit test class to test HTTP DELETE request to delete account using an account number.
 */
public class GetAccountDetailsTest {

	/**
	 * Runs before class to intialize and load H2 DB with preloaded values.
	 */
	@BeforeClass
	public static void setup() {
		DataSourceConfiguration.setupDataSource();

	}

	/**
	 * Test to check Fetch account details service is working.
	 * 
	 * Test case to pass if all the Account Details are returned.
	 */
	@Test
	public void testGetServiceReturnsValues() {

		MoneyTransferService getAccountInfo = new MoneyTransferService();
		Account account = getAccountInfo.getAccountDetails(101);
		assertEquals("Bharath", account.getFirstName());
		assertEquals("Duri", account.getLastName());
		assertEquals("Germany", account.getLocation());
		assertEquals(10000, account.getBalance(), 0.00001);
	}

	/**
	 * Test to check Fetch account details service Exception is handled for invalid
	 * account and printed in stacktrace.
	 * 
	 * Test case to pass if exception is handled.
	 */
	@Test
	public void testExceptionHandlingUsingInvalidAccount() {

		MoneyTransferService getAccountInfo = new MoneyTransferService();
		getAccountInfo.getAccountDetails(124);
	}

	/**
	 * Test Fetch account details after an account is created
	 * 
	 * Test to pass if get service fetches value of newly created Account
	 */
	@Test
	public void testServiceForNewAccount() {

		Account newAccount = new Account();
		newAccount.setAccountNumber(111);
		newAccount.setFirstName("Jamie");
		newAccount.setLastName("Doe");
		newAccount.setLocation("Germany");
		newAccount.setBalance(11000);

		MoneyTransferService testService = new MoneyTransferService();
		testService.createNewUser(newAccount);

		Account account = testService.getAccountDetails(newAccount.getAccountNumber());
		assertEquals(newAccount.getFirstName(), account.getFirstName());
		assertEquals(newAccount.getLastName(), account.getLastName());
		assertEquals(newAccount.getLocation(), account.getLocation());
		assertEquals(newAccount.getBalance(), account.getBalance(), 0.00001);
	}

}

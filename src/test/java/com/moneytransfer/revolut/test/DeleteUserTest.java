package com.moneytransfer.revolut.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.moneytransfer.configuration.DataSourceConfiguration;
import com.moneytransfer.model.ResponseMessage;
import com.moneytransfer.service.MoneyTransferService;

/**
 * @author bharathduri
 *
 */
public class DeleteUserTest {
	/**
	 * Runs before class to intialize and load H2 DB with preloaded values.
	 */
	@BeforeClass
	public static void setup() {
		DataSourceConfiguration.setupDataSource();

	}

	/**
	 * Test to check if delete service is working
	 * 
	 * Test to pass if user account is deleted.
	 */
	@Test
	public void deleteAccount() {

		MoneyTransferService deleteAccount = new MoneyTransferService();
		ResponseMessage response = deleteAccount.deleteUser(101);
		assertEquals("Account: " + 101 + "  deleted.", response.getResponseMessage());
	}

	/**
	 * Test to delete an account either deleted/non existing
	 * 
	 * Test to pass if ResponseMessage object is returned with "No account with
	 * account number: " + 101
	 */
	@Test
	public void deleteNonExistingAccount() {

		MoneyTransferService deleteAccount = new MoneyTransferService();
		ResponseMessage response = deleteAccount.deleteUser(101);
		assertEquals("No account with account number: " + 101, response.getResponseMessage());
	}

}

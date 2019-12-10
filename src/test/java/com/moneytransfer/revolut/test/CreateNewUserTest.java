package com.moneytransfer.revolut.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.moneytransfer.configuration.DataSourceConfiguration;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.ResponseMessage;
import com.moneytransfer.service.MoneyTransferService;

/**
 * @author bharathduri
 * 
 * 
 * Unit test class to test HTTP POST request to create a new User.
 *
 */
public class CreateNewUserTest {

	/**
	 * Runs before class to intialize and load H2 DB with preloaded values.
	 */
	@BeforeClass
	public static void setup() {
		DataSourceConfiguration.setupDataSource();

	}

	/**
	 * Test to Check create New User Service is working
	 * 
	 * Test to pass if new User is created and a ResponseMessage object is returned
	 * with success message
	 */
	@Test
	public void testCreateNewUser() {

		Account newAccount = new Account();
		newAccount.setAccountNumber(114);
		newAccount.setFirstName("Jamie");
		newAccount.setLastName("Doe");
		newAccount.setLocation("Germany");
		newAccount.setBalance(11000);

		MoneyTransferService createAccount = new MoneyTransferService();
		ResponseMessage response = createAccount.createNewUser(newAccount);
		System.out.println("" + response.getResponseMessage());
		assertEquals(114, response.getAccountNumber());
	}

	/**
	 * Test to check creation of duplicate User.
	 * 
	 * 
	 * Creating a duplicate user should return ResponseMessage with Account exists
	 * for account number: +account number
	 */
	@Test
	public void testDuplicateUserCreation() {

		Account newAccount = new Account();
		newAccount.setAccountNumber(114);
		newAccount.setFirstName("Jamie");
		newAccount.setLastName("Doe");
		newAccount.setLocation("Germany");
		newAccount.setBalance(11000);

		MoneyTransferService createAccount = new MoneyTransferService();
		ResponseMessage response = createAccount.createNewUser(newAccount);
		assertEquals("Account exists for account number: " + 114, response.getResponseMessage());
	}

}

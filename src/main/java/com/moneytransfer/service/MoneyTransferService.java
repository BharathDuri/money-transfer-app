package com.moneytransfer.service;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.moneytransfer.dao.AccountDetailsDAO;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.ResponseMessage;
import com.moneytransfer.model.TransferDetails;

/**
 * Root resource (exposed at "account" path)
 */
@Path("revolut")
public class MoneyTransferService{

	final static Logger logger = Logger.getLogger(AccountDetailsDAO.class);
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client.
     *
     * @return Account.
     */
    @Path("/accountDetails/{accountNumber}")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML})
	public Account getAccountDetails(@PathParam("accountNumber") int accountNumber) {
    	    logger.info("Fetching account details for account: " + accountNumber);
		AccountDetailsDAO accountDetails = new AccountDetailsDAO();
		return accountDetails.getAccount(accountNumber);
	}
    
    /**
     * Method handling HTTP PUT requests. The returned object will be sent
     * to the client.
     * 
     * @param transactiondetails
     * @return responseMessage
     */
    @Path("/transfer")
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML})
	public ResponseMessage transfer(TransferDetails transactiondetails) {

		logger.info("Intiating transfer from account: " + transactiondetails.getFromAccountNumber() + " to account: "
				+ transactiondetails.getToAccountNumber());
		AccountDetailsDAO accountDetailsDao = new AccountDetailsDAO();
		accountDetailsDao.transferAmount(transactiondetails);
		if (transactiondetails.getStatusMessage() == null) {
			transactiondetails.setStatusMessage("Successfully transferred " + transactiondetails.getAmount()
					+ " to account: " + transactiondetails.getToAccountNumber());
			logger.info(transactiondetails.getStatusMessage());
		}
		return dIsplayMessage(transactiondetails);
	}
    
    /**
     * Method handling HTTP POST requests. The returned object will be sent
     * to the client.
     * 
     * @param account
     * @return
     */
    @Path("/createNewUser")
    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML})
    public ResponseMessage createNewUser(Account account){
    	    logger.info("Creating new user for account number "+ account.getAccountNumber());
		AccountDetailsDAO accountDetails = new AccountDetailsDAO();
		return accountDetails.createUserAccount(account);
    }
    
    
    /**
     * Method handling HTTP POST requests. The returned object will be sent
     * to the client.
     * 
     * @param account
     * @return
     */
    @Path("/deleteUser/{accountNumber}")
    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML})
    public ResponseMessage deleteUser(@PathParam("accountNumber") int accountNumber){
        logger.info("Deleting user account: " + accountNumber);
		AccountDetailsDAO accountDetails = new AccountDetailsDAO();
		return accountDetails.deleteAccountUsingAccountNumber(accountNumber);
    }
    

	/**
	 * Method to Return success or failure messages
	 * 
	 * @param td
	 * @return responseMessage
	 */
	private ResponseMessage dIsplayMessage(TransferDetails transactiondetails) {
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setAccountNumber(transactiondetails.getFromAccountNumber());
		responseMessage.setResponseMessage(transactiondetails.getStatusMessage());
		return responseMessage;
	}
}

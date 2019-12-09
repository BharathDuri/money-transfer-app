package com.moneytransfer.revolut;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.moneytransfer.dao.AccountDetailsDAO;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.Response;
import com.moneytransfer.model.TransferDetails;

/**
 * Root resource (exposed at "account" path)
 */
@Path("revolut")
public class MoneyTransferResource{

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
	public Response transfer(TransferDetails transactiondetails) {
		AccountDetailsDAO accountDetailsDao = new AccountDetailsDAO();
		accountDetailsDao.transferAmount(transactiondetails);
		if(transactiondetails.getStatusMessage() == null) {
			transactiondetails.setStatusMessage("Successfully transferred");
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
    public Response createNewUser(Account account){
    	
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
    public Response deleteUser(@PathParam("accountNumber") int accountNumber){
    	
		AccountDetailsDAO accountDetails = new AccountDetailsDAO();
		return accountDetails.deleteAccount(accountNumber);
    }
    

	/**
	 * Method to Return success or failure messages
	 * 
	 * @param td
	 * @return responseMessage
	 */
	private Response dIsplayMessage(TransferDetails transactiondetails) {
		Response responseMessage = new Response();
		responseMessage.setFromAccountNumber(transactiondetails.getFromAccountNumber());
		responseMessage.setToAccountNumber(transactiondetails.getToAccountNumber());
		responseMessage.setResponseMessage(transactiondetails.getStatusMessage());
		return responseMessage;
	}
}

package com.moneytransfer.dao;

import org.apache.log4j.Logger;
import org.h2.jdbcx.JdbcConnectionPool;

import com.moneytransfer.configuration.DataSourceConfiguration;
import com.moneytransfer.model.Account;
import com.moneytransfer.model.ResponseMessage;
import com.moneytransfer.model.TransferDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author bharathduri
 * 
 * AccountDetailsDAO Class performs all in memory db operations to fetch Account Details, insert, update, delete Accounts
 *
 */
public class AccountDetailsDAO {

	static JdbcConnectionPool connectionPool = DataSourceConfiguration.getConnectionPool();
	final static Logger logger = Logger.getLogger(AccountDetailsDAO.class);

	/**
	 * 
	 * Method to get Account Object by passsing account number
	 * 
	 * @param accountNumber
	 * @return
	 */
	public Account getAccount(int accountNumber) {
		Account account = null;
		String sql = "SELECT ACCOUNT_NUMBER, FIRST_NAME, LAST_NAME,LOCATION, BALANCE FROM Account where ACCOUNT_NUMBER=?";
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = connectionPool.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, accountNumber);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				account = new Account();
				account.setAccountNumber(rs.getInt("ACCOUNT_NUMBER"));
				account.setFirstName(rs.getString("FIRST_NAME"));
				account.setLastName(rs.getString("LAST_NAME"));
				account.setLocation(rs.getString("LOCATION"));
				account.setBalance(rs.getDouble("BALANCE"));
				logger.debug("Account details: " + account);
			}else {
				throw new Exception("No account with account number: "+ accountNumber);
			}
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException exception) {
				exception.printStackTrace();
			}
		}
		return account;
	}

	/**
	 * 
	 * Method to Update Account table with the balance for an account number
	 * 
	 * @param conn
	 * @param accountNumber
	 * @param balance
	 * @return
	 */
	public int updateAccount(Connection conn, int accountNumber, double balance) {

		String sql = "UPDATE Account SET BALANCE=? where ACCOUNT_NUMBER=?";
		PreparedStatement stmt = null;
		try {
			// conn = connectionPool.getConnection();
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, balance);
			stmt.setInt(2, accountNumber);
			int count = stmt.executeUpdate();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}

		return 0;
	}

	/**
	 * Method for money transfer between two accounts
	 * 
	 * @param td
	 */
	public void transferAmount(TransferDetails td) {
		Connection conn = null;
		PreparedStatement fromStmt = null;
		ResultSet fromRs = null;
		PreparedStatement targetStmt = null;
		ResultSet targetRs = null;
		try {
			conn = connectionPool.getConnection();
			conn.setAutoCommit(false);
			fromStmt = conn.prepareStatement("SELECT * FROM Account WHERE ACCOUNT_NUMBER = ? FOR UPDATE");
			fromStmt.setLong(1, td.getFromAccountNumber());
			fromRs = fromStmt.executeQuery();
			targetStmt = conn.prepareStatement("SELECT * FROM Account WHERE ACCOUNT_NUMBER = ? FOR UPDATE");
			targetStmt.setLong(1, td.getToAccountNumber());
			targetRs = targetStmt.executeQuery();
			if (fromRs.next() && targetRs.next()) {
				double availableBalance = fromRs.getDouble("BALANCE");
				if (availableBalance < td.getAmount()) {
					logger.error("Insufficient Funds");
					throw new Exception("Insufficient Funds");
				}
				double newBalance = availableBalance - td.getAmount();
				int updatedRows = updateAccount(conn, td.getFromAccountNumber(), newBalance);
				if (updatedRows != 1) {
					throw new Exception("Failed to update the Sender's account balance");
				}
				newBalance = availableBalance + td.getAmount();
				updatedRows = updateAccount(conn, td.getToAccountNumber(), newBalance);
				if (updatedRows != 1) {
					throw new Exception("Failed to update the Receipient's account balance");
				}
			} else {
				throw new Exception("Target or From Accounts are invalid");
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			td.setStatusMessage(e.getMessage());
		} finally {
			try {
				if (targetStmt != null)
					targetStmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
			}
		}

	}

	/**
	 * Method to delete a user Account
	 * 
	 * @param accountNumber
	 * @return
	 */
	public ResponseMessage deleteAccountUsingAccountNumber(int accountNumber) {
		Connection conn = null;
		PreparedStatement fromStmt = null;
		ResultSet accountAvailableRs = null;
		PreparedStatement targetStmt = null;
		ResponseMessage response = new ResponseMessage();
		response.setAccountNumber(accountNumber);
		try {
			conn = connectionPool.getConnection();
			conn.setAutoCommit(false);
			fromStmt = conn.prepareStatement("SELECT * FROM Account WHERE ACCOUNT_NUMBER = ? FOR UPDATE");
			fromStmt.setLong(1, accountNumber);
			accountAvailableRs = fromStmt.executeQuery();
			if (accountAvailableRs.next()) {
				logger.debug("Account deleted for account number: " + accountNumber);
				boolean accountDelete = deleteAccount(conn, accountNumber);
				if (accountDelete) {
					response.setResponseMessage("Account: " + accountNumber + "  deleted.");
				} else {
					response.setResponseMessage("Unable to delete: " + accountNumber);
					logger.error(response.getResponseMessage());
				}
			} else {
				response.setResponseMessage("No account with account number: " + accountNumber);
				logger.error("No account with account number: " + accountNumber);
			}
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (targetStmt != null)
					targetStmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
			}
		}
		return response;
	}

	/**
	 * @param conn
	 * @param accountNumber
	 * @return boolean
	 */
	private boolean deleteAccount(Connection conn, int accountNumber) {

		String sql = "DELETE FROM Account where ACCOUNT_NUMBER=?";
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, accountNumber);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}
		return false;
	}

	
	 /** Method to create new User Account
	 * 
	 * @param account
	 * 
	 * @return
	 */
	public ResponseMessage createUserAccount(Account account) {
		Connection conn = null;
		PreparedStatement fromStmt = null;
		ResultSet accountAvailableRs = null;
		PreparedStatement targetStmt = null;
		ResponseMessage response = new ResponseMessage();
		response.setAccountNumber(account.getAccountNumber());
		try {
			conn = connectionPool.getConnection();
			conn.setAutoCommit(false);
			fromStmt = conn.prepareStatement("SELECT * FROM Account WHERE ACCOUNT_NUMBER = ? FOR UPDATE");
			fromStmt.setLong(1, account.getAccountNumber());
			accountAvailableRs = fromStmt.executeQuery();
			if (!accountAvailableRs.next()) {
				boolean accountInserted = insertAccount(conn, account);
				if (accountInserted) {
					response.setResponseMessage("Account for : " + account.getAccountNumber() + "  created.");
					logger.debug("New user account created :" + account);
				}
			} else {
				response.setResponseMessage("Account exists for account number: " + account.getAccountNumber());
				logger.error("User account exists!");
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			response.setResponseMessage("Error Insertring");
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (targetStmt != null)
					targetStmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
			}
		}
		return response;
	}

	/**
	 * @param conn
	 * @param account
	 * @return
	 */
	private boolean insertAccount(Connection conn, Account account) {

		String sql = "INSERT INTO Account(ACCOUNT_NUMBER, FIRST_NAME, LAST_NAME, LOCATION, BALANCE) VALUES (?,?,?,?,?);";
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, account.getAccountNumber());
			stmt.setString(2, account.getFirstName());
			stmt.setString(3, account.getLastName());
			stmt.setString(4, account.getLocation());
			stmt.setDouble(5, account.getBalance());
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
		}
		return false;
	}

}

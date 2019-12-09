/**
 * 
 */
package com.moneytransfer.revolut;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.moneytransfer.configuration.DataSourceConfiguration;
import com.moneytransfer.dao.AccountDetailsDAO;
import com.moneytransfer.service.MoneyTransferService;

/**
 * @author bharathduri
 * 
 * 
 * Run this class to Start the start a Jetty Server with inmemory h2 db.
 *
 */
public class App {
	
	final static Logger logger = Logger.getLogger(AccountDetailsDAO.class);
    private static final  int PORT_NUMBER = 8040;

	public static void main(String[] args) {
		try {
			// Set up in memory db configuration
			logger.debug("Setting up H2 memory db");
			DataSourceConfiguration.setupDataSource();
			// Start Server
			logger.debug("Starting Server");
			startStandAloneServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Method to start Jetty Server
     * 
     * @throws Exception
     */
    private static void startStandAloneServer() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(PORT_NUMBER);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                MoneyTransferService.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }



}

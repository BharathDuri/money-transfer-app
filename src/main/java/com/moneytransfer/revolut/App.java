/**
 * 
 */
package com.moneytransfer.revolut;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.moneytransfer.configuration.DataSourceConfiguration;

/**
 * @author bharathduri
 * 
 * 
 * Run this class to Start the start a Jetty Server with inmemory h2 db.
 *
 */
public class App {

    private static final  int PORT_NUMBER = 8040;

    public static void main(String[] args) {
        try {
        	    //Set up in memory db configuration
            DataSourceConfiguration.setupDataSource();
            //Start Server
            startStandAloneServer();
        } catch(Exception e) {
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
                MoneyTransferResource.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }



}

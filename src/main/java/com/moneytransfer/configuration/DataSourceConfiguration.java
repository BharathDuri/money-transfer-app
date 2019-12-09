package com.moneytransfer.configuration;

import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

/**
 * @author bharathduri
 *
 */
public class DataSourceConfiguration {

	//Intialize H2 variables
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/bank";

    static final String USER = "sa";
    static final String PASS = "";
    static final JdbcConnectionPool connectionPool = getConnectionPool();


    /**
     * 
     */
    public static void setupDataSource() {
        Connection conn = null;
        try {
            conn = getConnectionPool().getConnection();
            String filePath = DataSourceConfiguration.class.getClassLoader().getResource("db-init.sql").getFile();
            RunScript.execute(conn, new FileReader(new File(filePath)));
        } catch (FileNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return
     */
    public static JdbcConnectionPool getConnectionPool() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return JdbcConnectionPool.create(DB_URL, USER, PASS);
    }

}

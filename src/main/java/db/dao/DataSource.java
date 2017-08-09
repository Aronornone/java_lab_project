package db.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;
import utils.ServletLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {
    private static Logger log = ServletLog.getLgDB();
    private static String driverClassName;
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;
    private static int connPoolSize;
    private static int minIdleConnections;
    private static int maxIdleConnections;
    private static int maxOpenedPrepStmts;

    private static BasicDataSource dataSource;
    private static Connection connection;

    static {
        log.info("static block: Initiazling empty properties.");
        Properties properties = new Properties();

        log.info("static block: Trying to read properties from 'db.properties'.");
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("db.properties")))) {
            properties.load(br);
        } catch (IOException e) {
            log.error("static block: " + e);
        }

        log.info("static block: Initiazling DB related field parameters by those received from 'db.propertiesfs'.");
        driverClassName = properties.getProperty("driver");
        dbUrl = properties.getProperty("url");
        dbUser = properties.getProperty("user");
        dbPassword = properties.getProperty("password");
        connPoolSize = Integer.parseInt(properties.getProperty("connection_pool_size"));
        minIdleConnections = Integer.parseInt(properties.getProperty("min_idle_connections"));
        maxIdleConnections = Integer.parseInt(properties.getProperty("max_idle_connections"));
        maxOpenedPrepStmts = Integer.parseInt(properties.getProperty("max_opened_prepared_stmts"));
    }

    private static void getDataSource() {
        log.info("getDataSource(): Initializing dataSource field (of BasicDataSource apache class).");
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(driverClassName);
        bds.setUrl(dbUrl);
        bds.setUsername(dbUser);
        bds.setPassword(dbPassword);
        bds.setInitialSize(connPoolSize);
        bds.setMinIdle(minIdleConnections);
        bds.setMaxIdle(maxIdleConnections);
        bds.setMaxOpenPreparedStatements(maxOpenedPrepStmts);
        dataSource = bds;
    }

    public static Connection getConnection() {
        log.info("getConnection(): Trying to initialize connection.");
        try {
            if (connection == null || connection.isClosed()) {
                if (dataSource == null) {
                    DataSource.getDataSource();
                }
                connection = dataSource.getConnection();
            }
        } catch(SQLException e) {
            log.error("getConnection(): SQLException code: " + e.getErrorCode());
        }

        log.info("getConnection(): Returning connection variable.");
        return connection;
    }
}

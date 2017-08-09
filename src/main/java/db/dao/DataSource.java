package db.dao;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {
    private static final String DRIVER_CLASS_NAME;
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;
    private static final int CONN_POOL_SIZE;
    private static final int MIN_IDLE_CONNECTIONS;
    private static final int MAX_IDLE_CONNECTIONS;
    private static final int MAX_OPENED_PREP_STMTS;

    private static BasicDataSource dataSource;
    private static Connection connection;

    static {
        Properties properties = new Properties();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                    Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("db.properties")))) {
            properties.load(br);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DRIVER_CLASS_NAME = properties.getProperty("driver");
        DB_URL = properties.getProperty("url");
        DB_USER = properties.getProperty("user");
        DB_PASSWORD = properties.getProperty("password");
        CONN_POOL_SIZE = Integer.parseInt(properties.getProperty("connection_pool_size"));
        MIN_IDLE_CONNECTIONS = Integer.parseInt(properties.getProperty("min_idle_connections"));
        MAX_IDLE_CONNECTIONS = Integer.parseInt(properties.getProperty("max_idle_connections"));
        MAX_OPENED_PREP_STMTS = Integer.parseInt(properties.getProperty("max_opened_prepared_stmts"));
    }

    private static void getDataSource() {
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(DRIVER_CLASS_NAME);
        bds.setUrl(DB_URL);
        bds.setUsername(DB_USER);
        bds.setPassword(DB_PASSWORD);
        bds.setInitialSize(CONN_POOL_SIZE);
        bds.setMinIdle(MIN_IDLE_CONNECTIONS);
        bds.setMaxIdle(MAX_IDLE_CONNECTIONS);
        bds.setMaxOpenPreparedStatements(MAX_OPENED_PREP_STMTS);
        dataSource = bds;
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                if (dataSource == null) DataSource.getDataSource();
                connection = dataSource.getConnection();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}

package db;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;


public class DataSource {
    private static String DRIVER_CLASS_NAME;
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static int CONN_POOL_SIZE;
    private static int MIN_IDLE_CONNECTIONS;
    private static int MAX_IDLE_CONNECTIONS;
    private static int MAX_OPENED_PREP_STMTS;

    private static BasicDataSource dataSource;
    private static Connection connection;

    static {
        Properties properties = new Properties();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
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

    private static BasicDataSource getDataSource() {
        if (dataSource == null) {
            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(DRIVER_CLASS_NAME);
            ds.setUrl(DB_URL);
            ds.setUsername(DB_USER);
            ds.setPassword(DB_PASSWORD);
            ds.setInitialSize(CONN_POOL_SIZE);
            ds.setMinIdle(MIN_IDLE_CONNECTIONS);
            ds.setMaxIdle(MAX_IDLE_CONNECTIONS);
            ds.setMaxOpenPreparedStatements(MAX_OPENED_PREP_STMTS);
            dataSource = ds;
        }
        return dataSource;
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                dataSource = DataSource.getDataSource();
                connection = dataSource.getConnection();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}

package org.example.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static HikariDataSource dataSource;

    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.err.println("Sorry, unable to find application.properties");
            } else {
                prop.load(input);

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(prop.getProperty("db.url"));
                config.setUsername(prop.getProperty("db.username"));
                config.setPassword(prop.getProperty("db.password"));
                config.setDriverClassName(prop.getProperty("db.driver-class-name"));

                config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("db.hikari.maximum-pool-size", "10")));
                config.setMinimumIdle(Integer.parseInt(prop.getProperty("db.hikari.minimum-idle", "2")));
                config.setIdleTimeout(Long.parseLong(prop.getProperty("db.hikari.idle-timeout", "30000")));
                config.setConnectionTimeout(Long.parseLong(prop.getProperty("db.hikari.connection-timeout", "20000")));

                dataSource = new HikariDataSource(config);
            }
        } catch (IOException ex) {
            System.err.println("Error loading application.properties: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Database datasource is not initialized.");
        }
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}

package com.gylderia.polis.utils;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtil {
    private static HikariDataSource dataSource;

    public static void connect(HikariCPConfig config) {
        dataSource = config.getDataSource();
    }

    public static void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource != null) {
            return dataSource.getConnection();
        }
        throw new SQLException("HikariCP is not initialized.");
    }
}
package com.gylderia.polis.utils.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLAccess implements MySQLAPI {

    private FileConfiguration mysqlConfigFile;
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    /**
     * The HikariDataSource object that manages the connection pool.
     */
    private HikariDataSource dataSource;

    public MySQLAccess(FileConfiguration mysqlConfigFile) {
        this.mysqlConfigFile = mysqlConfigFile;
        this.host = mysqlConfigFile.getString("host");
        this.port = mysqlConfigFile.getString("port");
        this.database = mysqlConfigFile.getString("database");
        this.username = mysqlConfigFile.getString("username");
        this.password =  mysqlConfigFile.getString("password");
    }

    public boolean connect() {
        try {
            establishConnection(host, port, database, username, password);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Establishes a connection to a MySQL database using the provided parameters.
     * It uses the HikariCP library to manage the connection pool.
     *
     * @param host     The host of the MySQL server.
     * @param port     The port of the MySQL server.
     * @param database The name of the MySQL database.
     * @param username The username to connect to the MySQL server.
     * @param password The password to connect to the MySQL server.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public void establishConnection(String host, String port, String database, String username, String password) throws SQLException {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10); // Set the pool size here

        dataSource = new HikariDataSource(config);
    }

    /**
     * Gets a connection to the MySQL database from the connection pool.
     *
     * @return A connection to the MySQL database.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public Connection getConnection() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            connect();
        }
        return dataSource.getConnection();
    }

    /**
     * Closes the connection to the MySQL database and shuts down the connection pool.
     */
    @Override
    public void closeConnection() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
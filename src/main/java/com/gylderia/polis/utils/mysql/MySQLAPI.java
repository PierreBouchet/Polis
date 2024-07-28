package com.gylderia.polis.utils.mysql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This interface defines the MySQL API that can be used by other plugins.
 * It provides methods to establish a connection to a MySQL database,
 * get a connection, and close the connection.
 */
public interface MySQLAPI {

    /**
     * Establishes a connection to a MySQL database.
     *
     * @param host     The host of the MySQL server.
     * @param port     The port of the MySQL server.
     * @param database The name of the MySQL database.
     * @param username The username to connect to the MySQL server.
     * @param password The password to connect to the MySQL server.
     * @throws SQLException If a database access error occurs.
     */
    void establishConnection(String host, String port, String database, String username, String password) throws SQLException;

    /**
     * Gets a connection to the MySQL database.
     *
     * @return A connection to the MySQL database.
     * @throws SQLException If a database access error occurs.
     */
    Connection getConnection() throws SQLException;

    /**
     * Closes the connection to the MySQL database.
     */
    void closeConnection();
}
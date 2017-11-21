package org.chiclepad.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton managing database connection
 */
public enum DatabaseManager {

    /**
     * Singleton instance of the manager, to prevent multiple connections from 1 instance of the application
     */
    INSTANCE;

    /**
     * Logger for reporting errors, and important events
     */
    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    /**
     * Single connection
     */
    private Connection connection;

    /**
     * Loads the driver on initialization
     */
    private DatabaseManager() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed initializing postgresql driver. Check if you downloaded the driver " +
                    "using mvn install.", e);
        }
    }

    /**
     * @param file Properties file to load connection data from
     * @throws RuntimeException file not found or failed loading properties from
     */
    public void connect(File file) throws RuntimeException {
        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Provided file " + file.getAbsolutePath() + " doesn't exist. Please check if " +
                    "you used correct path", e);

        } catch (IOException e) {
            throw new RuntimeException("Provided file " + file.getAbsolutePath() + " failed loading. The file should " +
                    "have .properties format and contain host, port, database, username and password properties.");
        }

        connect(properties);
    }

    /**
     * @param properties Properties with connection data
     * @throws RuntimeException failed connecting to database
     */
    public void connect(Properties properties) throws RuntimeException {
        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String database = properties.getProperty("database");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        if (host == null || port == null || database == null || username == null || password == null) {
            throw new RuntimeException("Host: " + host + "\nPort: " + port + "\nDatabase: " + database + "\nUsername: "
                    + username + "\nPassword: " + password);
        }

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;

        try {
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            throw new RuntimeException("Failed connecting to database using url: " + url + ". check if your " +
                    "authentication data is correct.");
        }
    }

    /**
     * Should be checked every time before `getConnection()`
     *
     * @return Database connection was established
     */
    public boolean isConnected() {
        if (connection == null) {
            return false;
        }

        try {
            if (connection.isClosed()) {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed checking if connection " + connection + " to database was closed", e);
        }

        return true;
    }

    /**
     * @return Connection to database
     * @throws NullPointerException No connection was established
     * @throws RuntimeException     Connection doesnt't exist or error ocurred checking if it was closed
     */
    public Connection getConnection() throws RuntimeException {
        try {
            if (connection == null || connection.isClosed()) {
                throw new RuntimeException("No connection was established. Please run `void connect(Properties " +
                        "properties)` of the DatabaseManager class to connect to database");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed checking if connection " + connection + " to database was closed", e);
        }

        return connection;
    }

    /**
     * Closes the connection if possible
     *
     * @throws RuntimeException failed closing connection
     */
    public void close() throws RuntimeException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed closing connection " + connection, e);
        }
    }

}

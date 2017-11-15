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
            logger.error("Failed initializing postgresql driver. Check if you downloaded the driver using mvn install.");
            e.printStackTrace();
        }
    }

    /**
     * @param file Properties file to load connection data from
     */
    public void connect(File file) {
        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);

        } catch (FileNotFoundException e) {
            logger.error("Provided file " + file.getAbsolutePath() + " doesn't exist. Please check if you used correct path");
            e.printStackTrace();

        } catch (IOException e) {
            logger.error("Provided file " + file.getAbsolutePath() + " failed loading. The file should have .properties " +
                    "format and contain host, port, database, username and password properties.");
            e.printStackTrace();
        }

        connect(properties);
    }

    /**
     * @param properties Properties with connection data
     */
    public void connect(Properties properties) {
        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String database = properties.getProperty("database");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        if (host == null || port == null || database == null || username == null || password == null) {
            logger.error("Can't connect to database as not all needed properties were provided");
            throw new NullPointerException("Host: " + host + "\nPort: " + port + "\nDatabase: " + database + "\nUsername: " +
                    username + "\nPassword: " + password);
        }

        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;

        try {
            connection = DriverManager.getConnection(url, username, password);

        } catch (SQLException e) {
            logger.error("Failed connecting to database using url: " + url + ". check if your authentification data is correct.");
            e.printStackTrace();
        }
    }

    /**
     * @return Connection to database
     * @throws NullPointerException No connection was established
     * @throws SQLException database access error ocurred
     */
    public Connection getConnection() throws NullPointerException, SQLException {
        if (connection == null || connection.isClosed()) {
            throw new NullPointerException("No connection was established. Please run `void connect(Properties properties)`" +
                    " of the DatabaseManager class to connect to database");
        }

        return connection;
    }

    /**
     * Closes the connection if possible
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Failed closing connection " + connection);
            e.printStackTrace();
        }
    }

}

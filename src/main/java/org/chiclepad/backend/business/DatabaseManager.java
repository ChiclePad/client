package org.chiclepad.backend.business;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
     * Single connection
     */
    private JdbcTemplate jdbcTemplate;

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

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName(host);
        dataSource.setPortNumber(Integer.parseInt(port));
        dataSource.setDatabaseName(database);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Should be checked every time before `getConnection()`
     *
     * @return Database connection was established
     */
    public boolean isConnected() {
        return jdbcTemplate != null;
    }

    /**
     * @return Connection to database
     * @throws RuntimeException Connection doesn't exist or error occurred checking if it was closed
     */
    public JdbcTemplate getConnection() throws RuntimeException {
        if (jdbcTemplate == null) {
            throw new RuntimeException("No connection was established. Please run `void connect(Properties " +
                    "properties)` of the DatabaseManager class to connect to database");
        }

        return jdbcTemplate;
    }

}

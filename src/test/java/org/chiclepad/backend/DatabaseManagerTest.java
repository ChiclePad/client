package org.chiclepad.backend;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DatabaseManagerTest {

    private static final Properties goodProperties = new Properties();

    private static final Properties badProperties = new Properties();

    private static final Properties badInputProperties = new Properties();

    @BeforeAll
    private static void initializeProperties() {
        goodProperties.setProperty("host", "localhost");
        goodProperties.setProperty("port", "5432");
        goodProperties.setProperty("database", "chiclepad");
        goodProperties.setProperty("username", "postgres");
        goodProperties.setProperty("password", "root");

        badProperties.setProperty("host", "0.0.0.0");
        badProperties.setProperty("port", "22");
        badProperties.setProperty("database", "test");
        badProperties.setProperty("username", "root");
        badProperties.setProperty("password", "");

        badInputProperties.setProperty("host", "localhost");
        badInputProperties.setProperty("port", "5432");
        badInputProperties.setProperty("database", "chiclepad");
    }

    @Test
    void connect() throws SQLException {
        DatabaseManager.INSTANCE.connect(badProperties);
        assertThat(DatabaseManager.INSTANCE.getConnection()).isNotNull();

        assertThatThrownBy(() -> DatabaseManager.INSTANCE.connect(badInputProperties))
                .isInstanceOf(NullPointerException.class);

        try {
            DatabaseManager.INSTANCE.connect(goodProperties);
            DatabaseManager.INSTANCE.close();
        } catch (Exception e) {
            assertThat(false).isTrue();
        }
    }

    @Test
    void getConnection() {
//        DatabaseManager.INSTANCE.getConnection();
    }

    @Test
    void close() {
        DatabaseManager.INSTANCE.close();
    }
}
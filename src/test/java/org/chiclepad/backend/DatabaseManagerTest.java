package org.chiclepad.backend;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

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
    void connect() {
        assertThatThrownBy(() -> DatabaseManager.INSTANCE.connect(badProperties))
                .isInstanceOf(RuntimeException.class);

        assertThatThrownBy(() -> DatabaseManager.INSTANCE.connect(badInputProperties))
                .isInstanceOf(RuntimeException.class);

        assertThatCode(() -> {
            DatabaseManager.INSTANCE.connect(goodProperties);
            DatabaseManager.INSTANCE.close();
        }).doesNotThrowAnyException();
    }

    @Test
    void isConnected() {
        assertThatCode(() -> {
            assertThat(DatabaseManager.INSTANCE.isConnected()).isFalse();
            DatabaseManager.INSTANCE.connect(goodProperties);
            assertThat(DatabaseManager.INSTANCE.isConnected()).isTrue();
            DatabaseManager.INSTANCE.close();
            assertThat(DatabaseManager.INSTANCE.isConnected()).isFalse();

        }).doesNotThrowAnyException();
    }

    @Test
    void getConnection() {
        assertThatThrownBy(DatabaseManager.INSTANCE::getConnection)
                .isInstanceOf(RuntimeException.class);

        DatabaseManager.INSTANCE.connect(goodProperties);
        assertThat(DatabaseManager.INSTANCE.getConnection())
                .isNotNull()
                .isInstanceOf(Connection.class);

        DatabaseManager.INSTANCE.close();
    }

    @Test
    void close() {
        assertThatCode(() -> {
            DatabaseManager.INSTANCE.close();
            DatabaseManager.INSTANCE.connect(goodProperties);
            DatabaseManager.INSTANCE.close();
        }).doesNotThrowAnyException();

        assertThatThrownBy(DatabaseManager.INSTANCE::getConnection)
                .isInstanceOf(RuntimeException.class);
    }

}
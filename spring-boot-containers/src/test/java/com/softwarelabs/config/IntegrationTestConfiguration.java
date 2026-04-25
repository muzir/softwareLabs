package com.softwarelabs.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class IntegrationTestConfiguration {

    private static final String DB_NAME = "store";
    private static final String USERNAME = "dbuser";
    private static final String PASSWORD = "password";
    private static final String INIT_SCRIPT_PATH = "db/embedded-postgres-init.sql";

    @Bean
    @ServiceConnection // This MAGIC annotation replaces the manual DataSource bean
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine")
                .withInitScript(INIT_SCRIPT_PATH)
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .withDatabaseName(DB_NAME);
    }
}
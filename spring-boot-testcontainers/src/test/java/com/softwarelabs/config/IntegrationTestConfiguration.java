package com.softwarelabs.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class IntegrationTestConfiguration {

	private static final String DB_NAME = "store";
	private static final String USERNAME = "dbuser";
	private static final String PASSWORD = "password";

	@Bean(initMethod = "start")
	JdbcDatabaseContainer databaseContainer() {
		return new PostgreSQLContainer()
				.withInitScript("db/embedded-postgres-init.sql")
				.withDatabaseName("store");
	}

	@Bean
	@Primary
	DataSource dataSource(JdbcDatabaseContainer container) {

		System.out.println("Connecting to test container " + container.getUsername() + ":" + container.getPassword() + "@" + container.getJdbcUrl());

		int mappedPort = container.getMappedPort(5432);
		String mappedHost = container.getContainerIpAddress();

		final DataSource dataSource = DataSourceBuilder.create()
				.url("jdbc:postgresql://" + mappedHost + ":" + mappedPort + "/" + DB_NAME)
				.username(USERNAME)
				.password(PASSWORD)
				.driverClassName(container.getDriverClassName())
				.build();

		return dataSource;
	}
}

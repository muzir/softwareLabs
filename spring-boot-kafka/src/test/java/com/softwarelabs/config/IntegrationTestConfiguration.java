package com.softwarelabs.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
public class IntegrationTestConfiguration {

	private static final String DB_NAME = "store";
	private static final String USERNAME = "dbuser";
	private static final String PASSWORD = "password";
	private static final String PORT = "5432";
	private static final String INIT_SCRIPT_PATH="db/embedded-postgres-init.sql";



	@Bean(initMethod = "start")
	JdbcDatabaseContainer databaseContainer() {
		return new PostgreSQLContainer()
				.withInitScript(INIT_SCRIPT_PATH)
				.withUsername(USERNAME)
				.withPassword(PASSWORD)
				.withDatabaseName(DB_NAME);
	}

	@Bean
	@Primary
	DataSource dataSource(JdbcDatabaseContainer container) {

		System.out.println("Connecting to test container " + container.getUsername() + ":" + container.getPassword() + "@" + container.getJdbcUrl());

		int mappedPort = container.getMappedPort(Integer.parseInt(PORT));
		String mappedHost = container.getContainerIpAddress();

		final DataSource dataSource = DataSourceBuilder.create()
				.url("jdbc:postgresql://" + mappedHost + ":" + mappedPort + "/" + container.getDatabaseName())
				.username(container.getUsername())
				.password(container.getPassword())
				.driverClassName(container.getDriverClassName())
				.build();

		return dataSource;
	}
}

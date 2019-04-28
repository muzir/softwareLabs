package com.softwarelabs.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class IntegrationTestConfiguration {

	private static final String DB_NAME = "store";
	private static final String USERNAME = "dbuser";
	private static final String PASSWORD = "password";
	private static final String PORT = "5432";
	private static final String INIT_SCRIPT_PATH = "db/embedded-postgres-init.sql";

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

	@Bean(initMethod = "start")
	public KafkaContainer kafka() {
		return new KafkaContainer();
	}

	@Bean(name = "producerProps")
	public Map<String, Object> producerProps(KafkaContainer kafkaContainer) {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		return props;
	}

	@Bean(name = "consumerProps")
	public Map<String, Object> consumerProps(KafkaContainer kafkaContainer) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		return props;
	}
}

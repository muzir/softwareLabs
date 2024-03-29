package com.softwarelabs.kafka;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.*;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Slf4j
public class IntegrationTestConfiguration {


	// An alias that can be used to resolve the Toxiproxy container by name in the network it is connected to.
	// It can be used as a hostname of the Toxiproxy container by other containers in the same network.
	private static final String TOXIPROXY_NETWORK_ALIAS = "toxiproxy";
	private static final DockerImageName TOXIPROXY_IMAGE = DockerImageName.parse("ghcr.io/shopify/toxiproxy:2.7.0");
	private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka");
	private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres");
	private static final String DB_NAME = "store";
	private static final String USERNAME = "dbuser";
	private static final String PASSWORD = "password";
	private static final String PORT = "5432";
	private static final String INIT_SCRIPT_PATH = "db/embedded-postgres-init.sql";
	// Create a common docker network so that containers can communicate
	private Network network = Network.newNetwork();

	@Bean(initMethod = "start")
	PostgreSQLContainer<?> databaseContainer() {
		return new PostgreSQLContainer<>(POSTGRES_IMAGE)
				.withInitScript(INIT_SCRIPT_PATH)
				.withUsername(USERNAME)
				.withPassword(PASSWORD)
				.withDatabaseName(DB_NAME)
				.withNetwork(network)
				.withNetworkAliases("postgres");
	}

	@Bean
	ToxiproxyContainer.ContainerProxy jdbcDatabaseContainerProxy(JdbcDatabaseContainer container) {
		ToxiproxyContainer toxiproxyContainer = new ToxiproxyContainer(TOXIPROXY_IMAGE)
				.withNetwork(network)
				.withNetworkAliases(TOXIPROXY_NETWORK_ALIAS);
		toxiproxyContainer.start();
		return toxiproxyContainer.getProxy(container, Integer.parseInt(PORT));
	}

	@Bean
	@Primary
	DataSource dataSource(JdbcDatabaseContainer container, ToxiproxyContainer.ContainerProxy jdbcDatabaseContainerProxy) {
		final String ipAddressViaToxiproxy = jdbcDatabaseContainerProxy.getContainerIpAddress();
		final int portViaToxiproxy = jdbcDatabaseContainerProxy.getProxyPort();

		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl("jdbc:postgresql://" + ipAddressViaToxiproxy + ":" + portViaToxiproxy + "/" + container.getDatabaseName());
		dataSource.setUsername(container.getUsername());
		dataSource.setPassword(container.getPassword());
		dataSource.setDriverClassName(container.getDriverClassName());
		dataSource.setConnectionTimeout(3000);
		dataSource.setIdleTimeout(1000);
		dataSource.setValidationTimeout(1000);
		return dataSource;
	}

	@Bean(initMethod = "start")
	public KafkaContainer kafka() {
		return new KafkaContainer(KAFKA_IMAGE);
	}

	@Bean
	public Map<String, Object> producerProps(KafkaContainer kafkaContainer) {
		Map<String, Object> props = new ConcurrentHashMap<>();
		log.info("Kafka hashCode {}", kafkaContainer.hashCode());
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		return props;
	}

	@Bean
	public Map<String, Object> consumerProps(KafkaContainer kafkaContainer) {
		Map<String, Object> props = new ConcurrentHashMap<>();
		log.info("Kafka hashCode {}", kafkaContainer.hashCode());
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		return props;
	}

	@Bean
	public KafkaConsumerFactory<String, String> kafkaConsumerFactory() {
		return new KafkaConsumerFactory<>(consumerProps(kafka()));
	}

	@Bean
	public KafkaProducerFactory<String, String> kafkaProducerFactory() {
		return new KafkaProducerFactory<>(producerProps(kafka()));
	}
}

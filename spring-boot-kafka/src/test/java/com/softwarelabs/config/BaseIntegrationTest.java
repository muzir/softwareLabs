package com.softwarelabs.config;

import com.softwarelabs.App;
import org.junit.Rule;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;

@SpringBootTest(classes = App.class)
@ActiveProfiles("integration")
public abstract class BaseIntegrationTest {
}

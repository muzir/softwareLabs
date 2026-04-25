package com.softwarelabs.config;

import com.softwarelabs.App;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

@SpringBootTest(classes = App.class)
@ActiveProfiles("integration")
@Import(IntegrationTestConfiguration.class)
public class BaseIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String[] tablesToCleanUp = {"orders", "product", "queue_events"};

    @BeforeEach
    public void tearDown() {
        Arrays.stream(tablesToCleanUp).forEach(table -> {
            jdbcTemplate.execute("TRUNCATE TABLE " + table);
        });
    }
}

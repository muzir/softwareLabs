package com.softwarelabs.config;

import com.softwarelabs.App;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = App.class)
@ActiveProfiles("integration")
public abstract class BaseIntegrationTest {
}

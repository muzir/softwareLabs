package com.softwarelabs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.task.helper.RecurringTask;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.Schedule;
import com.softwarelabs.config.queue.QueueEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.Duration;
import java.util.List;

import static com.github.kagkarlsson.scheduler.task.schedule.FixedDelay.ofSeconds;

@Configuration
public class AppConfig {
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public RecurringTask createQueueEventProcessingTask(QueueEventService queueEventService) {
        return createRecurringTask(
                "queue-event-processing",
                ofSeconds(1),
                queueEventService::process
        );
    }

    private RecurringTask createRecurringTask(String name, Schedule schedule, Runnable action) {
        return Tasks.recurring(name, schedule).execute((inst, ctx) -> action.run());
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Scheduler kagTaskScheduler(DataSource dataSource, List<RecurringTask<?>> recurringTasks) {
        return Scheduler
                .create(dataSource)
                .startTasks(recurringTasks)
                .pollingInterval(Duration.ofSeconds(1))
                .threads(16)
                .heartbeatInterval(Duration.ofMinutes(2))
                .build();
    }
}

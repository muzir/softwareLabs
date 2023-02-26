package com.softwarelabs.config.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class QueueEventService {

    private List<QueueEventHandler> handlers;

    @Autowired
    public QueueEventService(List<QueueEventHandler> handlers) {
        this.handlers = handlers;
    }

    public void process(Collection<QueueEvent> queueEvents) {
        queueEvents.forEach(queueEvent -> {
            handlers.stream()
                    .filter(h -> h.match(queueEvent))
                    .forEach(queueEventHandler -> queueEventHandler.process(queueEvent));
        });
    }
}

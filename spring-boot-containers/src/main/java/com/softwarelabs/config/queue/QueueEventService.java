package com.softwarelabs.config.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueEventService {

    private final List<QueueEventHandler> handlers;

    private final QueueEventRepository queueEventRepository;

    @Autowired
    public QueueEventService(List<QueueEventHandler> handlers,
                             QueueEventRepository queueEventRepository) {
        this.handlers = handlers;
        this.queueEventRepository = queueEventRepository;
    }

    public void process() {
        var queueEvents = queueEventRepository.findAll();
        queueEvents.forEach(queueEvent -> {
            handlers.stream()
                    .filter(h -> h.match(queueEvent))
                    .forEach(queueEventHandler -> {
                        queueEventHandler.process(queueEvent);
                        queueEventRepository.delete(queueEvent.getId());
                    });
        });
    }
}

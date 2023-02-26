package com.softwarelabs.config.queue;

public interface QueueEventHandler {
    boolean match(QueueEvent queueEvent);

    void process(QueueEvent queueEvent);
}

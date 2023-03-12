package com.softwarelabs.config.queue;

public interface QueueEventHandler {

    String UPDATE_ORDER_NAME_OPERATION = "updateOrderName";
    String UPDATE_ORDER_STATUS_OPERATION = "updateOrderStatus";

    boolean match(QueueEvent queueEvent);

    void process(QueueEvent queueEvent);
}

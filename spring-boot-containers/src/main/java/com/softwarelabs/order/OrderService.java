package com.softwarelabs.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softwarelabs.order.command.UpdateOrderNameCommand;
import com.softwarelabs.order.command.UpdateOrderStatusCommand;

public interface OrderService {
    void updateStatusRequestWithOptimisticLocking(UpdateOrderStatusCommand updateOrderStatusCommand)
            throws JsonProcessingException;

    void updateNameRequestWithOptimisticLocking(UpdateOrderNameCommand updateOrderNameCommand)
            throws JsonProcessingException;
}

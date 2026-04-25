package com.softwarelabs.order;

import com.softwarelabs.order.command.UpdateOrderNameCommand;
import com.softwarelabs.order.command.UpdateOrderStatusCommand;

public interface OrderService {
    void updateStatusRequestWithOptimisticLocking(UpdateOrderStatusCommand updateOrderStatusCommand);

    void updateNameRequestWithOptimisticLocking(UpdateOrderNameCommand updateOrderNameCommand);
}

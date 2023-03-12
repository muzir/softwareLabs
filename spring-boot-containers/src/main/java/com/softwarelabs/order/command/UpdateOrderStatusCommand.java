package com.softwarelabs.order.command;

import com.softwarelabs.order.OrderStatus;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateOrderStatusCommand extends UpdateOrderCommand {
    private final OrderStatus status;

    public UpdateOrderStatusCommand() {
        this.status = null;
    }

    public UpdateOrderStatusCommand(UUID id, OrderStatus status) {
        super(id);
        this.status = status;
    }
}

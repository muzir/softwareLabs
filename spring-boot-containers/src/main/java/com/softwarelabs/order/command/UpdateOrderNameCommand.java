package com.softwarelabs.order.command;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateOrderNameCommand extends UpdateOrderCommand {
    private final String orderName;

    public UpdateOrderNameCommand(UUID id, String orderName) {
        super(id);
        this.orderName = orderName;
    }

    public UpdateOrderNameCommand() {
        this.orderName = null;
    }
}

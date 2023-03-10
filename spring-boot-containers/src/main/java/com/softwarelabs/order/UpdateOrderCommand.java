package com.softwarelabs.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UpdateOrderCommand {
    private String name;
    private OrderStatus status;
    private int version;
    private UUID id;

    public UpdateOrderCommand(String name, OrderStatus status, UUID id, int version) {
        this.name = name;
        this.status = status;
        this.id = id;
        this.version = version;
    }

    public UpdateOrderCommand(Order order) {
        this.name = order.getName();
        this.status = order.getStatus();
        this.id = order.getId();
        this.version = order.getVersion();
    }
}

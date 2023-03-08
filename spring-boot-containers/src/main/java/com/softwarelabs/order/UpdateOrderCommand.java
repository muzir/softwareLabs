package com.softwarelabs.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class UpdateOrderCommand {
    private String name;
    private OrderStatus status;
    private UUID id;

    public UpdateOrderCommand(String name, OrderStatus status, UUID id) {
        this.name = name;
        this.status = status;
        this.id = id;
    }
}

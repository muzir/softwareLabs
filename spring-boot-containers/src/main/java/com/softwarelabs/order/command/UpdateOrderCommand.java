package com.softwarelabs.order.command;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class UpdateOrderCommand {
    private final UUID id;

    public UpdateOrderCommand(UUID id) {
        this.id = id;
    }

    public UpdateOrderCommand() {
        this.id = null;
    }
}

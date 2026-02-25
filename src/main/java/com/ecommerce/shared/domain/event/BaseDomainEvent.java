package com.ecommerce.shared.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public abstract class BaseDomainEvent implements DomainEvent {
    private final UUID eventId;
    private final LocalDateTime occurredOn;
    private final String eventType;

    protected BaseDomainEvent() {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
        this.eventType = this.getClass().getSimpleName();
    }
}

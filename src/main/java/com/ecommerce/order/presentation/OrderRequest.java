package com.ecommerce.order.presentation;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderRequest(
        @NotNull UUID customerId
) {
}

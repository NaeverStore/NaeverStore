package com.naever.store.domain.order.dto

import jakarta.validation.constraints.Min

data class OrderItemRequest(
    val productId: Long,
    @field:Min(value = 1, message = "quantity must be at least 1")
    val quantity: Int,
)

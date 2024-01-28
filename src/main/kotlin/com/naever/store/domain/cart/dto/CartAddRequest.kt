package com.naever.store.domain.cart.dto

import jakarta.validation.constraints.Min

data class CartAddRequest(
    val productId: Long,
    @field:Min(value = 1, message = "양은 1 이상이야합니다.")
    val quantity: Int
)

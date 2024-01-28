package com.naever.store.domain.order.dto

import com.naever.store.domain.order.model.Order
import java.time.ZonedDateTime

data class OrderDetailResponse( // 주문한 후 보여줄 DTO
    val order: OrderResponse,
    val orderItems: List<OrderItemResponse>, // 주문한 상품(상품, 수량, 가격) 목록
)

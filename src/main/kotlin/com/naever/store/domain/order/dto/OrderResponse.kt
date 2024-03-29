package com.naever.store.domain.order.dto

import com.naever.store.domain.order.model.Order
import com.naever.store.domain.order.model.OrderStatus
import java.time.ZonedDateTime

data class OrderResponse(
    val id: Long?, // 주문번호
    val userId: Long?,
    val createdAt: ZonedDateTime, // 주문일자
    val status: OrderStatus, // Enum 타입으로 변경했어요
    val address: String, // 배송 주소,
    val totalPrice: Int
) {
    companion object {
        fun fromEntity(order: Order) : OrderResponse {
            return OrderResponse(
                id = order.id,
                userId = order.user.id,
                createdAt = order.createdAt,
                status = order.status,
                address = order.address,
                totalPrice = order.totalPrice
            )
        }
    }
}

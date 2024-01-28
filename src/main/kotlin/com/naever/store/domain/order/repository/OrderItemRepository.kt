package com.naever.store.domain.order.repository

import com.naever.store.domain.order.model.Order
import com.naever.store.domain.order.model.OrderItem
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository: JpaRepository<OrderItem, Long> {
    fun findByOrder(order: Order): List<OrderItem>
}
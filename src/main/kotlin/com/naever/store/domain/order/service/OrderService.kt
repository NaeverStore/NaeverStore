package com.naever.store.domain.order.service

import com.naever.store.domain.order.dto.*
import com.naever.store.infra.security.UserPrincipal

interface OrderService {

    fun findAllByUser(userId: Long): List<OrderDetailResponse>

    fun findById(userId: Long, orderId: Long): OrderDetailResponse

    fun createOrder(userId: Long, request: CreateOrderRequest): OrderDetailResponse

    fun updateOrder(userId: Long, orderId: Long, request: UpdateOrderRequest): OrderDetailResponse

    fun deleteOrder(userId: Long, orderId: Long): OrderDetailResponse
}


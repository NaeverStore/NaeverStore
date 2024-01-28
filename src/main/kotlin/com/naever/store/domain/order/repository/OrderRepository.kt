package com.naever.store.domain.order.repository

import com.naever.store.domain.order.model.Order
import com.naever.store.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface OrderRepository: JpaRepository<Order, Long> {
    fun findByUser(user: User): List<Order>
}
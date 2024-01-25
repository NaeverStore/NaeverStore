package com.naever.store.domain.order.controller

import com.naever.store.domain.order.dto.*
import com.naever.store.domain.order.service.OrderService
import com.naever.store.infra.security.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(private val orderItemService: OrderService) {

    @GetMapping
    fun findAll(): ResponseEntity<List<OrderDetailResponse>> {
        val orders = orderItemService.findAll()
        return ResponseEntity.status(HttpStatus.OK).body(orders)
    }

    @GetMapping("/{orderId}")
    fun findById(@PathVariable orderId: Long): ResponseEntity<OrderDetailResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(orderItemService.findById(orderId))
    }

    @PostMapping
    fun createOrder(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody orderRequest: CreateOrderRequest
    ): ResponseEntity<OrderDetailResponse> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(orderItemService.createOrder(userPrincipal.id, orderRequest))
    }

    @PutMapping("/{orderId}")
    fun updateOrder(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable orderId: Long,
        @RequestBody updateOrderRequest: UpdateOrderRequest): ResponseEntity<OrderDetailResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(orderItemService.updateOrder(userPrincipal.id, orderId, updateOrderRequest))
    }

    @DeleteMapping("/{orderId}")
    fun deleteOrder(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable orderId: Long): ResponseEntity<OrderDetailResponse> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(orderItemService.deleteOrder(userPrincipal.id, orderId))
    }

}
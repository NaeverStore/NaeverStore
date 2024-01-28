package com.naever.store.domain.cart.dto

import com.naever.store.domain.cart.model.Cart
import com.naever.store.domain.product.dto.ProductResponse

data class CartResponse(
    val productResponse: ProductResponse?,
    val userId: Long,
    val quantity: Int
)

{
    companion object {
        fun from(cart: Cart): CartResponse
        {
            return CartResponse(
                userId = cart.user.id!!,
                quantity = cart.quantity,
                productResponse = cart.product.let { ProductResponse.from(it) }
            )
        }
    }
}
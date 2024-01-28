package com.naever.store.domain.cart.service

import com.naever.store.domain.cart.dto.CartAddRequest
import com.naever.store.domain.cart.dto.CartDeleteRequest
import com.naever.store.domain.cart.dto.CartResponse
import com.naever.store.domain.cart.model.Cart
import com.naever.store.domain.cart.repository.CartRepository
import com.naever.store.domain.exception.ModelNotFoundException
import com.naever.store.domain.product.repository.IProductRepository
import com.naever.store.domain.user.repository.UserRepository
import com.naever.store.infra.security.SecurityUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartServiceImpl(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val productRepository: IProductRepository
): CartService {
    @Transactional
    override fun addItemInCart(cartAddRequest: CartAddRequest): CartResponse {
        val product = productRepository.findProductById(cartAddRequest.productId)
            ?: throw ModelNotFoundException("Product", cartAddRequest.productId)
        if(!product.availability) throw IllegalArgumentException("해당 제품을 이용할 수 없습니다.")
        val user = userRepository.findByIdOrNull(SecurityUtil.getLoginUserId())
            ?: throw ModelNotFoundException("User", SecurityUtil.getLoginUserId())
        val cart = CartResponse.from(
            cartRepository.findByUserIdAndProductId(SecurityUtil.getLoginUserId()!!, cartAddRequest.productId)
                ?.also { cart -> cart.apply { cart.quantity += cartAddRequest.quantity } } ?: cartRepository.save(
                Cart(
                    user = user,
                    product = product,
                    quantity = cartAddRequest.quantity
                ))
        )
        if(cart.quantity > product.stock)
            throw IllegalArgumentException("${product.itemName}을 ${cartAddRequest.quantity}만큼 넣을 수 없습니다.")
        return cart
    }

    override fun getCart(): List<CartResponse> {
        return cartRepository.findByUserId(SecurityUtil.getLoginUserId()!!).map { CartResponse.from(it) }
    }

    @Transactional
    override fun deleteItemInCart(cartDeleteRequest: CartDeleteRequest?) {
        cartDeleteRequest?.also { request ->
            request.deleteItems.forEach{cartRepository.deleteItemInCart(SecurityUtil.getLoginUserId()!!, it)} }
            ?: cartRepository.deleteAllByUserId(SecurityUtil.getLoginUserId()!!)
    }
}
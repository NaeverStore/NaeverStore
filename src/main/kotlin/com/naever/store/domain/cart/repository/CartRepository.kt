package com.naever.store.domain.cart.repository

import com.naever.store.domain.cart.model.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface CartRepository: JpaRepository<Cart, Long> {
    @Modifying
    @Query("delete from Cart c where c.user.id = :userId")
    fun deleteAllByUserId(userId: Long)
    @Modifying
    @Query("delete from Cart c where c.user.id = :userId and c.product.id = :itemId")
    fun deleteItemInCart(userId: Long, itemId: Long)
    fun findByUserIdAndProductId(userId: Long, itemId: Long): Cart?
    fun findByUserId(userId: Long): List<Cart>
    @Modifying
    @Query("delete from Cart c where c.user.id = :userId and c.product.id in :itemIds")
    fun deleteItemsInCart(userId: Long, itemIds: List<Long>)
}
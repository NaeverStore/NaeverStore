package com.naever.store.domain.product.model

import com.naever.store.common.BaseTimeEntity
import com.naever.store.domain.product.dto.ProductRequest
import com.naever.store.domain.store.model.Store
import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "product")
@SQLRestriction("is_deleted <> true")
class Product(

    @Column(name = "item_name")
    var itemName: String,

    @Column(name = "availability")
    var availability: Boolean = true,

    @Column(name = "stock")
    var stock: Int,

    @Column(name = "price")
    var price: Int,

    @Column(name = "sales")
    var sales: Int = 0,

    @Column(name = "description")
    var description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    val store: Store

) : BaseTimeEntity() {
    init {
        if (stock < 1) {
            throw IllegalArgumentException("stock must be at least 1")
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun matchStoreId(requestStoreId: Long): Boolean {
        return store.id == requestStoreId
    }

    fun updateProduct(request: ProductRequest) {
        itemName = request.itemName
        price = request.price
        description = request.description
        stock = request.stock
    }

    fun order(quantity: Int) {
        if (stock - quantity < 0) {
            throw IllegalStateException("$itemName (id: $id) out of stock")
        }
        stock -= quantity
        sales += quantity
        changeAvailability()
    }

    fun cancelOrder(quantity: Int) {
        stock += quantity
        sales -= quantity
        changeAvailability()
    }

    private fun changeAvailability() {
        availability = stock > 0
    }

    fun deleteProduct() {
        isDeleted = true
    }

}
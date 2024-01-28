package com.naever.store.domain.product.dto

import com.naever.store.domain.product.model.Product

data class ProductResponse(
    val id: Long?,
    val itemName: String,
    val price: Int,
    val description: String,
    val stock: Int,
    val sales: Int,
    val availability: Boolean,
    val storeName: String,
) {
    companion object {
        fun from(product: Product) : ProductResponse {
            return ProductResponse(
                id = product.id,
                itemName = product.itemName,
                price = product.price,
                description = product.description,
                stock = product.stock,
                sales = product.sales,
                availability = product.availability,
                storeName = product.store.name
            )
        }
    }
}

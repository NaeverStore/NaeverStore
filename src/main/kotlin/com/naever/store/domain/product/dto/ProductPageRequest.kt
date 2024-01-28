package com.naever.store.domain.product.dto

data class ProductPageRequest(
    val sort: String?,
    val itemName: String?,
    val startPrice: Int?,
    val endPrice: Int?,
    val storeId: Long?
)

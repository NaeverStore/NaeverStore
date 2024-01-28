package com.naever.store.domain.product.repository

import com.naever.store.domain.product.dto.ProductPageRequest
import com.naever.store.domain.product.dto.ProductPageResponse
import com.naever.store.domain.product.model.Product

interface IProductRepository {

    fun save(product: Product): Product

    fun getPaginatedProductList(pageNumber: Int, pageSize: Int, request: ProductPageRequest): ProductPageResponse

    fun findProductById(id: Long): Product?

    fun deleteProductById(id: Long)

    fun deleteAllByStoreId(storeId: Long)

}
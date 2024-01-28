package com.naever.store.domain.product.service

import com.naever.store.domain.product.dto.ProductPageRequest
import com.naever.store.domain.product.dto.ProductPageResponse
import com.naever.store.domain.product.dto.ProductRequest
import com.naever.store.domain.product.dto.ProductResponse

interface ProductService {

    fun getProductList(pageNumber: Int, pageSize: Int, request: ProductPageRequest): ProductPageResponse

    fun getProductById(productId: Long): ProductResponse

    fun registerProduct(storeId: Long, request: ProductRequest): ProductResponse

    fun updateProduct(storeId: Long, productId: Long, request: ProductRequest): ProductResponse

    fun deleteProduct(storeId: Long, productId: Long)

}
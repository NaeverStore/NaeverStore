package com.naever.store.domain.product.service

import com.naever.store.domain.exception.ForbiddenException
import com.naever.store.domain.exception.ModelNotFoundException
import com.naever.store.domain.product.dto.ProductPageRequest
import com.naever.store.domain.product.dto.ProductPageResponse
import com.naever.store.domain.product.dto.ProductRequest
import com.naever.store.domain.product.dto.ProductResponse
import com.naever.store.domain.product.model.Product
import com.naever.store.domain.product.repository.IProductRepository
import com.naever.store.domain.store.service.StoreService
import com.naever.store.infra.security.SecurityUtil
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl(
    private val productRepository: IProductRepository,
    private val storeService: StoreService
) : ProductService {

    override fun getProductList(pageNumber: Int, pageSize: Int, request: ProductPageRequest): ProductPageResponse {

        return productRepository.getPaginatedProductList(pageNumber, pageSize, request)
    }

    override fun getProductById(productId: Long): ProductResponse {

        val product = productRepository.findProductById(productId) ?: throw ModelNotFoundException("Product", productId)

        return ProductResponse.from(product)
    }

    override fun registerProduct(storeId: Long, request: ProductRequest): ProductResponse {

        val store = storeService.getStoreIfAuthorized(SecurityUtil.getLoginUserId(), storeId)

        return Product(
            itemName = request.itemName,
            price = request.price,
            stock = request.stock,
            description = request.description,
            store = store
        ).let {
            productRepository.save(it)
        }.let { ProductResponse.from(it) }
    }

    override fun updateProduct(storeId: Long, productId: Long, request: ProductRequest): ProductResponse {

        val product = getProductIfAuthorized(storeId, productId)

        product.updateProduct(request)

        return productRepository.save(product)
            .let { ProductResponse.from(it) }
    }

    override fun deleteProduct(storeId: Long, productId: Long) {

        val product = getProductIfAuthorized(storeId, productId)

        product.deleteProduct()

        productRepository.save(product)
    }

    private fun getProductIfAuthorized(storeId: Long, productId: Long): Product {

        val store = storeService.getStoreIfAuthorized(SecurityUtil.getLoginUserId(), storeId)

        val product = productRepository.findProductById(productId) ?: throw ModelNotFoundException("Product", productId)

        if (!product.matchStoreId(store.id!!)) {
            throw ForbiddenException(SecurityUtil.getLoginUserId()!!, "Product", productId)
        }

        return product
    }

}
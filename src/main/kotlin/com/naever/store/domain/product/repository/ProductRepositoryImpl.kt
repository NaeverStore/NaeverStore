package com.naever.store.domain.product.repository

import com.naever.store.domain.product.dto.ProductPageRequest
import com.naever.store.domain.product.dto.ProductPageResponse
import com.naever.store.domain.product.dto.ProductResponse
import com.naever.store.domain.product.model.Product
import com.naever.store.domain.product.model.QProduct
import com.naever.store.domain.store.model.QStore
import com.naever.store.domain.user.model.QUser.user
import com.naever.store.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import org.springframework.data.repository.findByIdOrNull
import kotlin.math.ceil

class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository
) : IProductRepository, QueryDslSupport() {

    private val product = QProduct.product
    private val store = QStore.store

    override fun save(product: Product): Product {
        return productJpaRepository.save(product)
    }

    override fun getPaginatedProductList(
        pageNumber: Int,
        pageSize: Int,
        request: ProductPageRequest
    ): ProductPageResponse {

        val (sort, itemName, startPrice, endPrice, storeId) = request

        val whereClause = BooleanBuilder()
        itemName?.let { whereClause.and(product.itemName.contains(it)) }
        startPrice?.let { whereClause.and(product.price.goe(it)) }
        endPrice?.let { whereClause.and(product.price.loe(it)) }
        storeId?.let { whereClause.and(product.store.id.eq(it)) }

        val query = queryFactory.select(product)
            .from(product)
            .leftJoin(product.store, store).fetchJoin()
            .leftJoin(product.store.user, user).fetchJoin()
            .where(whereClause)

        val totalPages = ceil(query.fetch().size / pageSize.toDouble()).toInt()

        when (sort) {
            "price_low" -> {
                query.orderBy(product.price.asc(), product.id.desc())
            }

            "price_high" -> {
                query.orderBy(product.price.desc(), product.id.desc())
            }

            else -> {
                query.orderBy(product.id.desc())
            }
        }

        query
            .offset(((pageNumber - 1) * pageSize).toLong())
            .limit(pageSize.toLong())

        return ProductPageResponse(
            pageResult = query.fetch().map { ProductResponse.from(it) },
            totalPages = totalPages
        )
    }

    override fun findProductById(id: Long): Product? {
        return productJpaRepository.findByIdOrNull(id)
    }

    override fun deleteProductById(id: Long) {
        productJpaRepository.deleteById(id)
    }

    override fun deleteAllByStoreId(storeId: Long) {
        queryFactory.update(product)
            .set(product.isDeleted, true)
            .where(product.store.id.eq(storeId))
            .execute()
    }

}
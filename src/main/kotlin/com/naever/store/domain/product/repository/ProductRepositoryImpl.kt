package com.naever.store.domain.product.repository

import com.naever.store.domain.product.dto.ProductPageRequest
import com.naever.store.domain.product.dto.ProductPageResponse
import com.naever.store.domain.product.dto.ProductResponse
import com.naever.store.domain.product.model.Product
import com.naever.store.domain.product.model.QProduct
import com.naever.store.domain.user.model.QUser
import com.naever.store.infra.querydsl.QueryDslSupport
import com.querydsl.core.BooleanBuilder
import kotlin.math.ceil

class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository
) : IProductRepository, QueryDslSupport() {

    private val product = QProduct.product
    private val user = QUser.user

    override fun save(product: Product): Product {
        return productJpaRepository.save(product)
    }

    override fun getPaginatedProductList(
        pageNumber: Int,
        pageSize: Int,
        request: ProductPageRequest
    ): ProductPageResponse {

        val (sort, itemName, startPrice, endPrice) = request

        val whereClause = BooleanBuilder()
        itemName?.let { whereClause.and(product.itemName.contains(itemName)) }
        startPrice?.let { whereClause.and(product.price.goe(startPrice)) }
        endPrice?.let { whereClause.and(product.price.loe(endPrice)) }

        val query = queryFactory.select(product)
            .from(product)
//            .leftJoin(product.user, user).fetchJoin()
            .where(whereClause)

        val totalPages = ceil(query.fetch().size / pageSize.toDouble()).toInt()

        query
            .offset(((pageNumber - 1) * pageSize).toLong())
            .limit(pageSize.toLong())

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

        return ProductPageResponse(
            pageResult = query.fetch().map { ProductResponse.from(it) },
            totalPages = totalPages
        )
    }

}
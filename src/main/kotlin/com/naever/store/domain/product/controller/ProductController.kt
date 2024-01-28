package com.naever.store.domain.product.controller

import com.naever.store.domain.product.dto.ProductPageRequest
import com.naever.store.domain.product.dto.ProductPageResponse
import com.naever.store.domain.product.dto.ProductResponse
import com.naever.store.domain.product.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController(
    private val productService: ProductService
) {

    @GetMapping
    fun getAllProductList(
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "5") pageSize: Int,
        request: ProductPageRequest
    ): ResponseEntity<ProductPageResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(productService.getProductList(pageNumber, pageSize, request))
    }

    @GetMapping("/{productId}")
    fun getProductById(@PathVariable productId: Long): ResponseEntity<ProductResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(productService.getProductById(productId))
    }

}
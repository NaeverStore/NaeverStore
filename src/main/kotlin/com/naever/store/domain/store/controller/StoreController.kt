package com.naever.store.domain.store.controller

import com.naever.store.domain.product.dto.ProductPageRequest
import com.naever.store.domain.product.dto.ProductPageResponse
import com.naever.store.domain.product.dto.ProductRequest
import com.naever.store.domain.product.dto.ProductResponse
import com.naever.store.domain.product.service.ProductService
import com.naever.store.domain.store.dto.StoreRequest
import com.naever.store.domain.store.dto.StoreResponse
import com.naever.store.domain.store.service.StoreService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stores")
class StoreController(
    private val storeService: StoreService,
    private val productService: ProductService
) {

    @PostMapping
    fun createStore(@RequestBody request: StoreRequest): ResponseEntity<StoreResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(storeService.createStore(request))
    }

    @GetMapping("/{storeId}")
    fun getStore(@PathVariable storeId: Long): ResponseEntity<StoreResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.getStore(storeId))
    }

    @PutMapping("/{storeId}")
    fun updateStore(@PathVariable storeId: Long, @RequestBody request: StoreRequest): ResponseEntity<StoreResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(storeService.updateStore(storeId, request))
    }

    @DeleteMapping("/{storeId}")
    fun deleteStore(@PathVariable storeId: Long): ResponseEntity<Unit> {
        storeService.deleteStore(storeId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

    @GetMapping("/{storeId}/products")
    fun getProductList(
        @PathVariable storeId: Long,
        @RequestParam(defaultValue = "1") pageNumber: Int,
        @RequestParam(defaultValue = "5") pageSize: Int,
        request: ProductPageRequest
    ): ResponseEntity<ProductPageResponse> {

        val request = ProductPageRequest(
            sort = request.sort,
            itemName = request.itemName,
            startPrice = request.startPrice,
            endPrice = request.endPrice,
            storeId = storeId
        )

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(productService.getProductList(pageNumber, pageSize, request))
    }

    @PostMapping("/{storeId}/products")
    fun registerProduct(
        @PathVariable storeId: Long,
        @RequestBody request: ProductRequest
    ): ResponseEntity<ProductResponse> {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(productService.registerProduct(storeId, request))
    }

    @PutMapping("/{storeId}/products/{productId}")
    fun updateProduct(
        @PathVariable storeId: Long,
        @PathVariable productId: Long,
        @RequestBody request: ProductRequest
    ): ResponseEntity<ProductResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(productService.updateProduct(storeId, productId, request))
    }

    @DeleteMapping("/{storeId}/products/{productId}")
    fun deleteProduct(
        @PathVariable storeId: Long,
        @PathVariable productId: Long
    ): ResponseEntity<Unit> {

        productService.deleteProduct(storeId, productId)

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }

}

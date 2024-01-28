package com.naever.store.domain.cart.controller

import com.naever.store.domain.cart.dto.CartAddRequest
import com.naever.store.domain.cart.dto.CartDeleteRequest
import com.naever.store.domain.cart.dto.CartResponse
import com.naever.store.domain.cart.service.CartService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/carts")
class CartController(
    private val cartService: CartService
) {
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping
    fun addItem(@Valid @RequestBody cartAddRequest: CartAddRequest,
                bindingResult: BindingResult): ResponseEntity<CartResponse>
    {
        if(bindingResult.hasErrors())
        {
            throw IllegalArgumentException(bindingResult.fieldError?.defaultMessage ?: "")
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(cartService.addItemInCart(cartAddRequest))
    }
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @DeleteMapping
    fun deleteCartItem(@RequestBody(required = false) deleteItemsRequest: CartDeleteRequest?): ResponseEntity<Unit>
    {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(cartService.deleteItemInCart(deleteItemsRequest))
    }
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    fun getCartItems(): ResponseEntity<List<CartResponse>>
    {
        return ResponseEntity.status(HttpStatus.OK)
            .body(cartService.getCart())
    }
}
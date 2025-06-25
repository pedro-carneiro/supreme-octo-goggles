package io.github.recipe.controller

import io.github.recipe.domain.AddRecipeToCartRequest
import io.github.recipe.domain.Cart
import io.github.recipe.service.CartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CartController(
    private val cartService: CartService,
) {
    @GetMapping("/carts/{cartId}")
    suspend fun getCart(
        @PathVariable("cartId") cartId: Long,
    ): ResponseEntity<Cart> = ResponseEntity.ofNullable(cartService.getCart(cartId))

    @PostMapping("/carts/{cartId}/add_recipe")
    suspend fun addRecipeToCart(
        @PathVariable("cartId") cartId: Long,
        @RequestBody request: AddRecipeToCartRequest,
    ): ResponseEntity<Void> {
        cartService.addRecipeToCart(cartId = cartId, recipeId = request.recipeId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/carts/{cartId}/recipes/{recipeId}")
    suspend fun deleteRecipeFromCart(
        @PathVariable("cartId") cartId: Long,
        @PathVariable("recipeId") recipeId: Long,
    ): ResponseEntity<Void> {
        cartService.deleteRecipeFromCart(cartId = cartId, recipeId = recipeId)
        return ResponseEntity.ok().build()
    }
}

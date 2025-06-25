package io.github.recipe.repository.entity

data class CartProduct(
    val cartId: Long,
    val totalInCents: Long,
    val productId: Long,
    val productName: String,
    val priceInCents: Long,
    val quantity: Int,
)

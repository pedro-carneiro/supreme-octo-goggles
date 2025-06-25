package io.github.recipe.domain

data class Cart(
    val id: Long,
    val products: List<Product>,
    val totalInCents: Long,
)

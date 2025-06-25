package io.github.recipe.domain

data class Product(
    val id: Long,
    val name: String,
    val priceInCents: Long,
    val quantity: Int,
)

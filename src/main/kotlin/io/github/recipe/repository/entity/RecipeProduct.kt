package io.github.recipe.repository.entity

data class RecipeProduct(
    val recipeId: Long,
    val recipeName: String,
    val productId: Long,
    val productName: String,
    val priceInCents: Long,
    val quantity: Int,
)

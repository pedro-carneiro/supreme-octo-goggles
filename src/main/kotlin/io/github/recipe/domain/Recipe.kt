package io.github.recipe.domain

data class Recipe(
    val name: String,
    val products: Set<Product>,
)

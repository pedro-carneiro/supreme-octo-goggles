package io.github.recipe.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "recipe_items", schema = "recipe")
data class RecipeItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,
    @Column(name = "recipe_id", nullable = false)
    val recipeId: Long,
    @Column(name = "product_id", nullable = false)
    val productId: Long,
    @Column(name = "quantity", nullable = false)
    val quantity: Int,
)

package io.github.recipe.repository

import io.github.recipe.repository.entity.RecipeEntity
import io.github.recipe.repository.entity.RecipeProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<RecipeEntity, Long> {
    @Query(
        value = """
        SELECT ri.recipe_id, r.name recipe_name, ri.product_id, p.name product_name, p.price_in_cents, ri.quantity
          FROM recipe.recipes r
          JOIN recipe.recipe_items ri ON r.id = ri.recipe_id
          JOIN recipe.products p ON ri.product_id = p.id
    """,
        nativeQuery = true,
    )
    fun getAllRecipesAndProducts(): List<RecipeProduct>

    @Query(
        value = """
        SELECT ri.recipe_id, r.name recipe_name, ri.product_id, p.name product_name, p.price_in_cents, ri.quantity
          FROM recipe.recipes r
          JOIN recipe.recipe_items ri ON r.id = ri.recipe_id
          JOIN recipe.products p ON ri.product_id = p.id
         WHERE r.id = :recipeId
    """,
        nativeQuery = true,
    )
    fun getRecipeProducts(recipeId: Long): List<RecipeProduct>
}

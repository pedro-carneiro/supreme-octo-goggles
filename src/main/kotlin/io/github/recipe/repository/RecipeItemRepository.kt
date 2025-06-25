package io.github.recipe.repository

import io.github.recipe.repository.entity.RecipeItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeItemRepository : JpaRepository<RecipeItemEntity, Long> {
    fun findAllByRecipeId(recipeId: Long): List<RecipeItemEntity>
}

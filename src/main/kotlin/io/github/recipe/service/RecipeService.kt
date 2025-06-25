package io.github.recipe.service

import io.github.recipe.domain.Product
import io.github.recipe.domain.Recipe
import io.github.recipe.repository.RecipeRepository
import io.github.recipe.repository.SqlTransactionHelper
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val sqlTransactionHelper: SqlTransactionHelper,
    private val recipeRepository: RecipeRepository,
) {
    suspend fun listRecipes(): List<Recipe> {
        val recipeProducts = sqlTransactionHelper.execute(isReadOnly = true) {
            recipeRepository.getAllRecipesAndProducts()
        }
        return recipeProducts.groupBy { it.recipeId }.map { (_, recipeProducts) ->
            Recipe(
                name = recipeProducts.first().recipeName,
                products = recipeProducts.map { recipeProduct ->
                    Product(
                        id = recipeProduct.productId,
                        name = recipeProduct.productName,
                        priceInCents = recipeProduct.priceInCents,
                        quantity = recipeProduct.quantity,
                    )
                }.toSet(),
            )
        }
    }
}

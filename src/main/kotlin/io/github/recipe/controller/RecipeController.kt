package io.github.recipe.controller

import io.github.recipe.domain.Recipe
import io.github.recipe.service.RecipeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RecipeController(
    private val recipeService: RecipeService,
) {
    @GetMapping("/recipes")
    suspend fun listRecipes(): List<Recipe> = recipeService.listRecipes()
}

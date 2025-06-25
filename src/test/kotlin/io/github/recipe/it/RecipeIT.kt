package io.github.recipe.it

import io.github.recipe.Resources.getResource
import org.junit.jupiter.api.Test
import org.springframework.test.json.JsonCompareMode

class RecipeIT : AbstractIntegrationTest() {
    @Test
    fun `should retrieve list of recipes`() {
        client
            .get()
            .uri("/recipes")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json(getResource("/expectations/recipes.json"), JsonCompareMode.STRICT)
    }
}

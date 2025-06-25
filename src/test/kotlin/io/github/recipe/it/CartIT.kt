package io.github.recipe.it

import io.github.recipe.Resources.getResource
import io.github.recipe.repository.CartItemRepository
import io.github.recipe.repository.CartRepository
import io.github.recipe.repository.entity.CartEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.json.JsonCompareMode

class CartIT : AbstractIntegrationTest() {
    @Autowired
    private lateinit var cartItemRepository: CartItemRepository

    @Autowired
    private lateinit var cartRepository: CartRepository

    @Test
    fun `should retrieve an empty cart`() {
        verifyCart("/expectations/empty-cart.json")
    }

    @Test
    fun `should return not found when using unrecognised cart ID`() {
        client
            .get()
            .uri("/carts/333")
            .exchange()
            .expectStatus()
            .isNotFound
    }

    @Test
    fun `should add and remove recipes to cart`() {
        try {
            addRecipe(1L)

            verifyCart("/expectations/cart-with-recipe-1.json")

            addRecipe(2L)

            verifyCart("/expectations/cart-with-recipe-1-and-2.json")

            removeRecipe(1L)

            verifyCart("/expectations/cart-with-recipe-2.json")

            removeRecipe(2L)

            verifyCart("/expectations/empty-cart.json")
        } finally {
            cartItemRepository.deleteAll()
            cartRepository.save(CartEntity(id = 1L, totalInCents = 0L))
        }
    }

    private fun addRecipe(recipeId: Long) {
        client
            .post()
            .uri("/carts/1/add_recipe")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"recipeId":$recipeId}""")
            .exchange()
            .expectStatus()
            .isCreated
    }

    private fun removeRecipe(recipeId: Long) {
        client
            .delete()
            .uri("/carts/1/recipes/$recipeId")
            .exchange()
            .expectStatus()
            .isOk
    }

    private fun verifyCart(expectation: String) {
        client
            .get()
            .uri("/carts/1")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .json(getResource(expectation), JsonCompareMode.STRICT)
    }
}

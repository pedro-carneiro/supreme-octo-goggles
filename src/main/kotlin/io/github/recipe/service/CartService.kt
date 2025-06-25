package io.github.recipe.service

import io.github.recipe.domain.Cart
import io.github.recipe.domain.Product
import io.github.recipe.repository.CartItemRepository
import io.github.recipe.repository.CartRepository
import io.github.recipe.repository.ProductRepository
import io.github.recipe.repository.RecipeItemRepository
import io.github.recipe.repository.SqlTransactionHelper
import io.github.recipe.repository.entity.CartEntity
import io.github.recipe.repository.entity.CartItemEntity
import io.github.recipe.repository.entity.RecipeItemEntity
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class CartService(
    private val sqlTransactionHelper: SqlTransactionHelper,
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository,
    private val recipeItemRepository: RecipeItemRepository,
) {
    suspend fun getCart(cartId: Long): Cart {
        val cart = retrieveCart(cartId)

        val cartProducts = sqlTransactionHelper.execute(isReadOnly = true) { cartRepository.getCartProducts(cartId) }

        return Cart(
            id = cart.id,
            products = cartProducts.map { recipeProduct ->
                Product(
                    id = recipeProduct.productId,
                    name = recipeProduct.productName,
                    priceInCents = recipeProduct.priceInCents,
                    quantity = recipeProduct.quantity,
                )
            },
            totalInCents = cart.totalInCents,
        )
    }

    suspend fun addRecipeToCart(
        cartId: Long,
        recipeId: Long,
    ) = recipeToCart(cartId = cartId, recipeId = recipeId, operation = Int::plus)

    suspend fun deleteRecipeFromCart(
        cartId: Long,
        recipeId: Long,
    ) = recipeToCart(cartId = cartId, recipeId = recipeId, operation = Int::minus)

    /**
     * Shared implementation for adding and removing recipe to cart.
     * @param operation Operation to apply on quantities (add when adding recipes, subtract when removing them).
     */
    private suspend fun recipeToCart(
        cartId: Long,
        recipeId: Long,
        operation: (Int, Int) -> Int,
    ) {
        val cart = retrieveCart(cartId)

        val recipeItems = getRecipeItems(recipeId).ifEmpty { return }
        val cartItems = getCartItems(cartId)

        val cartItemsMap = cartItems.associateBy { it.productId }.toMutableMap()

        recipeItems.forEach {
            var cartItem = cartItemsMap[it.productId]
            // if item exists in cart, update its quantity
            // otherwise, create new item
            cartItem = cartItem?.copy(quantity = operation(cartItem.quantity, it.quantity))
                ?: CartItemEntity(id = null, cartId = cartId, productId = it.productId, quantity = it.quantity)
            // update item to map
            cartItemsMap[it.productId] = cartItem
        }

        val total = getCartTotal(cartItemsMap.values)

        // if item has no quantity, it can be removed from cart
        val (itemsToRemove, itemsToUpsert) = cartItemsMap.values.partition { it.quantity == 0 }

        saveCart(cart = cart.copy(totalInCents = total), itemsToUpsert = itemsToUpsert, itemsToRemove = itemsToRemove)
    }

    private suspend fun retrieveCart(cartId: Long): CartEntity = sqlTransactionHelper.execute(isReadOnly = true) {
        cartRepository.findById(cartId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found!")
        }
    }

    private suspend fun getCartItems(cartId: Long): List<CartItemEntity> =
        sqlTransactionHelper.execute(isReadOnly = true) { cartItemRepository.findAllByCartId(cartId) }

    private suspend fun getRecipeItems(recipeId: Long): List<RecipeItemEntity> =
        sqlTransactionHelper.execute(isReadOnly = true) { recipeItemRepository.findAllByRecipeId(recipeId) }

    /**
     * Determines the total price of a cart, given the items that are in it.
     */
    private suspend fun getCartTotal(items: Collection<CartItemEntity>): Long {
        // retrieve all products in cart
        val productIds = items.map { it.productId }
        val products = sqlTransactionHelper.execute(isReadOnly = true) { productRepository.findAllById(productIds) }
        val productsMap = products.associateBy { it.id }

        // accumulate price * quantity for every item
        return items.fold(0L) { acc, item ->
            val price = productsMap[item.productId]!!.priceInCents
            acc + (price * item.quantity)
        }
    }

    private suspend fun saveCart(
        cart: CartEntity,
        itemsToUpsert: Collection<CartItemEntity>,
        itemsToRemove: Collection<CartItemEntity>,
    ) = sqlTransactionHelper.execute(isReadOnly = false) {
        cartItemRepository.deleteAll(itemsToRemove)
        cartItemRepository.saveAll(itemsToUpsert)
        cartRepository.save(cart)
    }
}

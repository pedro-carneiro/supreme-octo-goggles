package io.github.recipe.repository

import io.github.recipe.repository.entity.CartEntity
import io.github.recipe.repository.entity.CartProduct
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CartRepository : JpaRepository<CartEntity, Long> {
    @Query(
        value = """
        SELECT ci.cart_id, c.total_in_cents, ci.product_id, p.name, p.price_in_cents, ci.quantity
          FROM recipe.carts c
          JOIN recipe.cart_items ci ON ci.cart_id = c.id
          JOIN recipe.products p ON ci.product_id = p.id
         WHERE c.id = :cartId
    """,
        nativeQuery = true,
    )
    fun getCartProducts(cartId: Long): List<CartProduct>
}

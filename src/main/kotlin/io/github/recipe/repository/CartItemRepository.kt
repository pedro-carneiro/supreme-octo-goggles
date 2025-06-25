package io.github.recipe.repository

import io.github.recipe.repository.entity.CartItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartItemRepository : JpaRepository<CartItemEntity, Long> {
    fun findAllByCartId(cartId: Long): List<CartItemEntity>
}

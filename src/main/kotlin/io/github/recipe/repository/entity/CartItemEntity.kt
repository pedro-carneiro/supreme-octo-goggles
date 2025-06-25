package io.github.recipe.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "cart_items", schema = "recipe")
data class CartItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,
    @Column(name = "cart_id", nullable = false)
    val cartId: Long,
    @Column(name = "product_id", nullable = false)
    val productId: Long,
    @Column(name = "quantity", nullable = false)
    val quantity: Int,
)

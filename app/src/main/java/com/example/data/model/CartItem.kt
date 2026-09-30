package com.example.data.model

data class CartItem(
    val food: FoodItem,
    val quantity: Int
) {
    val totalPrice: Long get() = food.price * quantity
}

data class ReceiptItem(
    val foodName: String,
    val unitPrice: Long,
    val quantity: Int,
    val totalPrice: Long
)

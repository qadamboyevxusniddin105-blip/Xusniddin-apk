package com.example.data.model

data class FoodItem(
    val id: Int,
    val nameUz: String,
    val category: FoodCategory,
    val price: Long, // in UZS (so'm)
    val description: String,
    val ingredients: String,
    val portion: String,
    val prepTimeMinutes: Int,
    val calories: Int,
    val rating: Float,
    val emoji: String,
    val isSpecial: Boolean = false,
    val popularTag: String? = null
)

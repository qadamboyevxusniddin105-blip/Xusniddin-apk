package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderNumber: String,
    val timestamp: Long,
    val customerName: String,
    val customerPhone: String,
    val orderType: String, // "Yetkazib berish", "Olib ketish", "Restoranda"
    val deliveryAddressOrTable: String,
    val paymentMethod: String, // "Naqd pul", "Karta (Click / Payme)"
    val orderNotes: String,
    val itemsSummary: String, // JSON or formatted readable text of items
    val subtotal: Long,
    val serviceFee: Long,
    val deliveryFee: Long,
    val grandTotal: Long,
    val status: String // "Qabul qilindi", "Tayyorlanmoqda", "Yetkazildi"
)

package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val cookieId: String,
    val quantity: Int,
    val note: String = ""
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val createdAt: Long = System.currentTimeMillis(),
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val deliveryNotes: String = "",
    val deliveryMethod: String = "Kurir Instan (30-45 Menit)",
    val deliveryFee: Int = 15000,
    val promoCode: String = "",
    val discountAmount: Int = 0,
    val subtotal: Int = 0,
    val totalAmount: Int = 0,
    val paymentMethod: String = "QRIS", // "QRIS", "BCA Virtual Account", "Mandiri Virtual Account", "GoPay", "ShopeePay", "DANA", "Kartu Kredit"
    val paymentStatus: String = "PAID", // "PENDING", "PAID"
    val orderStatus: String = "CONFIRMED", // "WAITING_PAYMENT", "CONFIRMED", "BAKING", "PACKAGING", "DELIVERING", "DELIVERED"
    val courierName: String = "Ahmad Pratama",
    val courierPhone: String = "0812-9844-1290",
    val courierPlate: String = "B 3918 KXX (Honda Vario)",
    val estimatedMinutesRemaining: Int = 25,
    val itemsSummary: String = "" // formatted summary: "2x Blue Monster, 1x Red Velvet"
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: String,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "ORDER_STATUS" // "PAYMENT", "BAKING", "DELIVERY", "DELIVERED", "PROMO"
)

package com.example.data.repository

import com.example.data.local.MawCookiesDatabase
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

class CookieRepository(private val database: MawCookiesDatabase) {
    private val cartDao = database.cartDao()
    private val orderDao = database.orderDao()
    private val notificationDao = database.notificationDao()

    val cartItems: Flow<List<CartItemEntity>> = cartDao.getAllCartItems()
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    val activeOrder: Flow<OrderEntity?> = orderDao.getActiveOrder()
    val notifications: Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()
    val unreadNotificationCount: Flow<Int> = notificationDao.getUnreadCount()

    fun getOrderById(orderId: String): Flow<OrderEntity?> = orderDao.getOrderById(orderId)

    suspend fun getOrderByIdDirect(orderId: String): OrderEntity? = orderDao.getOrderByIdDirect(orderId)

    suspend fun addToCart(cookieId: String, quantity: Int = 1, note: String = "") {
        cartDao.insertOrUpdate(CartItemEntity(cookieId = cookieId, quantity = quantity, note = note))
    }

    suspend fun updateCartQuantity(cookieId: String, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteByCookieId(cookieId)
        } else {
            cartDao.updateQuantity(cookieId, quantity)
        }
    }

    suspend fun removeFromCart(cookieId: String) {
        cartDao.deleteByCookieId(cookieId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    suspend fun createOrder(order: OrderEntity) {
        orderDao.insertOrder(order)
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }

    suspend fun updateOrder(order: OrderEntity) {
        orderDao.updateOrder(order)
    }

    suspend fun markOrderAsPaid(orderId: String) {
        orderDao.markOrderAsPaid(orderId)
    }

    suspend fun addNotification(orderId: String, title: String, message: String, type: String) {
        notificationDao.insertNotification(
            NotificationEntity(
                orderId = orderId,
                title = title,
                message = message,
                timestamp = System.currentTimeMillis(),
                type = type
            )
        )
    }

    suspend fun markNotificationRead(id: Long) {
        notificationDao.markAsRead(id)
    }

    suspend fun markAllNotificationsRead() {
        notificationDao.markAllAsRead()
    }
}

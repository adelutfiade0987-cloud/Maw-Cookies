package com.example.ui

import android.app.Application
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MawCookiesDatabase
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.model.CookieCatalog
import com.example.data.model.CookieProduct
import com.example.data.repository.CookieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.random.Random

data class CartItemUi(
    val product: CookieProduct,
    val quantity: Int,
    val note: String
)

data class PromoOption(
    val code: String,
    val title: String,
    val description: String,
    val discountPercent: Int = 0,
    val maxDiscount: Int = 0,
    val minSpend: Int = 0,
    val isFreeDelivery: Boolean = false
)

enum class AppNavTab {
    KATALOG, PESANAN, NOTIFIKASI, TENTANG
}

class MawCookiesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = MawCookiesDatabase.getInstance(application)
    private val repository = CookieRepository(database)

    // Current navigation state
    private val _currentTab = MutableStateFlow(AppNavTab.KATALOG)
    val currentTab: StateFlow<AppNavTab> = _currentTab.asStateFlow()

    // Sub-screens (Detail, Cart, Checkout, Payment, Tracking)
    private val _currentScreen = MutableStateFlow("MAIN")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _selectedProduct = MutableStateFlow<CookieProduct?>(null)
    val selectedProduct: StateFlow<CookieProduct?> = _selectedProduct.asStateFlow()

    private val _activeTrackingOrderId = MutableStateFlow<String?>(null)
    val activeTrackingOrderId: StateFlow<String?> = _activeTrackingOrderId.asStateFlow()

    // Search and filter
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Semua")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Checkout form state
    val customerName = MutableStateFlow("Adelutfi")
    val customerPhone = MutableStateFlow("0812-8765-4321")
    val customerAddress = MutableStateFlow("Apartemen Sudirman Tower Lt. 12 No. 8B, Jl. Jend. Sudirman, Jakarta Selatan")
    val customerDeliveryNote = MutableStateFlow("Tolong titipkan di resepsionis lobi utama.")
    val selectedDeliveryMethod = MutableStateFlow("Kurir Instan (30-45 Menit)")
    val selectedPaymentMethod = MutableStateFlow("QRIS")

    // Promo code state
    private val _appliedPromo = MutableStateFlow<PromoOption?>(null)
    val appliedPromo: StateFlow<PromoOption?> = _appliedPromo.asStateFlow()
    val promoInput = MutableStateFlow("")

    val availablePromos = listOf(
        PromoOption(
            code = "MAWCHUNKY",
            title = "Diskon Rp 10.000",
            description = "Potongan Rp 10.000 minimal belanja Rp 50.000",
            discountPercent = 0,
            maxDiscount = 10000,
            minSpend = 50000
        ),
        PromoOption(
            code = "FREESHIP",
            title = "Gratis Ongkir Instan",
            description = "Gratis ongkos kirim hingga Rp 15.000",
            isFreeDelivery = true,
            minSpend = 60000
        ),
        PromoOption(
            code = "SWEET20",
            title = "Diskon 20% Manis",
            description = "Diskon 20% tanpa minimum belanja (Maks Rp 25.000)",
            discountPercent = 20,
            maxDiscount = 25000,
            minSpend = 0
        )
    )

    // Cart items from Room combined with Product Catalog
    val cartItemsUi: StateFlow<List<CartItemUi>> = repository.cartItems
        .combine(_selectedCategory) { entities, _ ->
            entities.mapNotNull { entity ->
                CookieCatalog.getProductById(entity.cookieId)?.let { product ->
                    CartItemUi(product = product, quantity = entity.quantity, note = entity.note)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All and active orders
    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeOrder: StateFlow<OrderEntity?> = repository.activeOrder
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Notifications
    val notifications: StateFlow<List<NotificationEntity>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationCount: StateFlow<Int> = repository.unreadNotificationCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Toast/Alert message events
    private val _uiEvents = MutableSharedFlow<String>()
    val uiEvents: SharedFlow<String> = _uiEvents.asSharedFlow()

    // Tracking simulation job
    private var trackingSimulationJob: Job? = null

    init {
        // Seed a welcoming promo notification if empty
        viewModelScope.launch {
            delay(500)
            if (database.notificationDao().getAllNotifications().stateIn(this).value.isEmpty()) {
                repository.addNotification(
                    orderId = "PROMO-WELCOME",
                    title = "Selamat Datang di Maw Cookies! 🍪",
                    message = "Gunakan kode promo 'MAWCHUNKY' untuk potongan Rp 10.000 pada pesanan pertamamu!",
                    type = "PROMO"
                )
            }
        }
    }

    fun setTab(tab: AppNavTab) {
        _currentTab.value = tab
        _currentScreen.value = "MAIN"
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
    }

    fun openProductDetail(product: CookieProduct) {
        _selectedProduct.value = product
        _currentScreen.value = "PRODUCT_DETAIL"
    }

    fun closeProductDetail() {
        _selectedProduct.value = null
        _currentScreen.value = "MAIN"
    }

    fun openCart() {
        _currentScreen.value = "CART"
    }

    fun openCheckout() {
        _currentScreen.value = "CHECKOUT"
    }

    fun openTracking(orderId: String) {
        _activeTrackingOrderId.value = orderId
        _currentScreen.value = "ORDER_TRACKING"
    }

    fun navigateBack() {
        when (_currentScreen.value) {
            "PRODUCT_DETAIL" -> {
                _selectedProduct.value = null
                _currentScreen.value = "MAIN"
            }
            "CART" -> _currentScreen.value = "MAIN"
            "CHECKOUT" -> _currentScreen.value = "CART"
            "PAYMENT_SIMULATION" -> _currentScreen.value = "CHECKOUT"
            "ORDER_TRACKING" -> {
                _currentScreen.value = "MAIN"
                _currentTab.value = AppNavTab.PESANAN
            }
            else -> _currentScreen.value = "MAIN"
        }
    }

    // Cart Actions
    fun addToCart(cookieId: String, qtyChange: Int = 1) {
        viewModelScope.launch {
            val current = cartItemsUi.value.find { it.product.id == cookieId }
            val currentQty = current?.quantity ?: 0
            val newQty = currentQty + qtyChange
            repository.updateCartQuantity(cookieId, newQty)

            if (qtyChange > 0 && currentQty == 0) {
                _uiEvents.emit("Ditambahkan ke keranjang 🍪")
            }
        }
    }

    fun updateCartQuantity(cookieId: String, newQty: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cookieId, newQty)
        }
    }

    fun removeFromCart(cookieId: String) {
        viewModelScope.launch {
            repository.removeFromCart(cookieId)
            _uiEvents.emit("Produk dihapus dari keranjang")
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Promo Validation
    fun applyPromo(code: String) {
        val promo = availablePromos.find { it.code.equals(code.trim(), ignoreCase = true) }
        if (promo == null) {
            viewModelScope.launch { _uiEvents.emit("Kode promo tidak valid!") }
            return
        }

        val subtotal = calculateSubtotal()
        if (subtotal < promo.minSpend) {
            viewModelScope.launch {
                _uiEvents.emit("Minimal belanja untuk promo ini adalah ${formatRupiah(promo.minSpend)}")
            }
            return
        }

        _appliedPromo.value = promo
        promoInput.value = promo.code
        viewModelScope.launch { _uiEvents.emit("Promo '${promo.code}' berhasil dipasang! 🎉") }
    }

    fun removePromo() {
        _appliedPromo.value = null
        promoInput.value = ""
        viewModelScope.launch { _uiEvents.emit("Promo dibatalkan") }
    }

    // Price Calculations
    fun calculateSubtotal(): Int {
        return cartItemsUi.value.sumOf { it.product.price * it.quantity }
    }

    fun calculateDeliveryFee(): Int {
        if (_appliedPromo.value?.isFreeDelivery == true) return 0
        return when (selectedDeliveryMethod.value) {
            "Kurir Instan (30-45 Menit)" -> 15000
            "Same Day Delivery (2-4 Jam)" -> 10000
            "Ambil di Toko (Pickup Fresh)" -> 0
            else -> 15000
        }
    }

    fun calculateDiscount(): Int {
        val promo = _appliedPromo.value ?: return 0
        val subtotal = calculateSubtotal()
        if (promo.isFreeDelivery) {
            return when (selectedDeliveryMethod.value) {
                "Kurir Instan (30-45 Menit)" -> 15000
                "Same Day Delivery (2-4 Jam)" -> 10000
                else -> 0
            }
        }
        if (promo.maxDiscount > 0 && promo.discountPercent == 0) {
            return promo.maxDiscount.coerceAtMost(subtotal)
        }
        if (promo.discountPercent > 0) {
            val calc = (subtotal * promo.discountPercent) / 100
            return if (promo.maxDiscount > 0) calc.coerceAtMost(promo.maxDiscount) else calc
        }
        return 0
    }

    fun calculateTotal(): Int {
        val subtotal = calculateSubtotal()
        val delivery = calculateDeliveryFee()
        val discount = calculateDiscount()
        return (subtotal + delivery - discount).coerceAtLeast(0)
    }

    // Checkout & Order Creation
    fun processCheckout() {
        if (cartItemsUi.value.isEmpty()) return

        val orderId = "MAW-" + (10000..99999).random()
        val itemsSummary = cartItemsUi.value.joinToString(", ") { "${it.quantity}x ${it.product.name}" }
        val subtotal = calculateSubtotal()
        val deliveryFee = calculateDeliveryFee()
        val discount = calculateDiscount()
        val total = calculateTotal()

        val newOrder = OrderEntity(
            orderId = orderId,
            createdAt = System.currentTimeMillis(),
            customerName = customerName.value.ifBlank { "Pelanggan Maw" },
            customerPhone = customerPhone.value.ifBlank { "0812-0000-0000" },
            deliveryAddress = customerAddress.value.ifBlank { "Alamat Pengantaran" },
            deliveryNotes = customerDeliveryNote.value,
            deliveryMethod = selectedDeliveryMethod.value,
            deliveryFee = deliveryFee,
            promoCode = _appliedPromo.value?.code ?: "",
            discountAmount = discount,
            subtotal = subtotal,
            totalAmount = total,
            paymentMethod = selectedPaymentMethod.value,
            paymentStatus = "MENUNGGU",
            orderStatus = "WAITING_PAYMENT",
            courierName = "Ahmad Pratama",
            courierPhone = "0812-9844-1290",
            courierPlate = "B 3918 KXX (Vario)",
            estimatedMinutesRemaining = 35,
            itemsSummary = itemsSummary
        )

        viewModelScope.launch {
            repository.createOrder(newOrder)
            repository.clearCart()
            _activeTrackingOrderId.value = orderId
            _currentScreen.value = "PAYMENT_SIMULATION"
            _appliedPromo.value = null
            promoInput.value = ""

            repository.addNotification(
                orderId = orderId,
                title = "Tagihan Pesanan $orderId Dibuat",
                message = "Selesaikan pembayaran sebesar ${formatRupiah(total)} menggunakan ${newOrder.paymentMethod}.",
                type = "ORDER_STATUS"
            )
        }
    }

    // Payment Confirmation Simulation
    fun confirmPayment(orderId: String) {
        viewModelScope.launch {
            repository.markOrderAsPaid(orderId)
            triggerVibration()
            repository.addNotification(
                orderId = orderId,
                title = "Pembayaran Diterima! ✅",
                message = "Pesanan $orderId berhasil diverifikasi. Tim dapur Maw Cookies mulai menyiapkan pesananmu.",
                type = "PAYMENT"
            )
            _uiEvents.emit("Pembayaran Berhasil! Pesanan Dikonfirmasi 🍪")
            _currentScreen.value = "ORDER_TRACKING"

            // Start auto progression for tracking
            startOrderProgressionSimulation(orderId)
        }
    }

    // Advance order to next status manually or automatically
    fun advanceOrderStatus(orderId: String) {
        viewModelScope.launch {
            val order = repository.getOrderByIdDirect(orderId) ?: return@launch
            val nextStatus = when (order.orderStatus) {
                "WAITING_PAYMENT" -> "CONFIRMED"
                "CONFIRMED" -> "BAKING"
                "BAKING" -> "PACKAGING"
                "PACKAGING" -> "DELIVERING"
                "DELIVERING" -> "DELIVERED"
                else -> "DELIVERED"
            }

            if (nextStatus != order.orderStatus) {
                repository.updateOrderStatus(orderId, nextStatus)
                triggerVibration()

                val (title, message) = when (nextStatus) {
                    "CONFIRMED" -> Pair(
                        "Pesanan Dikonfirmasi 📝",
                        "Dapur Maw Cookies telah menerima tiket pesananmu."
                    )
                    "BAKING" -> Pair(
                        "Cookies Sedang Dipanggang! ♨️",
                        "Aroma harum semerbak! Adonan cookies segarmu sedang dipanggang di oven."
                    )
                    "PACKAGING" -> Pair(
                        "Pengemasan Box Eksklusif 📦",
                        "Cookies baru matang telah didinginkan dan dikemas rapat dalam kotak higienis."
                    )
                    "DELIVERING" -> Pair(
                        "Kurir Sedang Mengantar 🛵",
                        "Kurir ${order.courierName} (${order.courierPlate}) meluncur ke alamatmu!"
                    )
                    "DELIVERED" -> Pair(
                        "Pesanan Telah Sampai! 🎉",
                        "Cookies hangat Maw Cookies siap dinikmati. Selamat menikmati kelezatan lembutnya!"
                    )
                    else -> Pair("Status Pesanan", "Status diperbarui.")
                }

                repository.addNotification(
                    orderId = orderId,
                    title = title,
                    message = message,
                    type = nextStatus
                )
                _uiEvents.emit(title)
            }
        }
    }

    private fun startOrderProgressionSimulation(orderId: String) {
        trackingSimulationJob?.cancel()
        trackingSimulationJob = viewModelScope.launch {
            // Wait 12 seconds then move to BAKING
            delay(12000)
            advanceOrderStatus(orderId)

            // Wait 15 seconds then move to PACKAGING
            delay(15000)
            advanceOrderStatus(orderId)

            // Wait 18 seconds then move to DELIVERING
            delay(18000)
            advanceOrderStatus(orderId)

            // Wait 20 seconds then move to DELIVERED
            delay(20000)
            advanceOrderStatus(orderId)
        }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
        }
    }

    private fun triggerVibration() {
        try {
            val vibrator = getApplication<Application>().getSystemService(Vibrator::class.java)
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } catch (_: Exception) {}
    }

    companion object {
        fun formatRupiah(amount: Int): String {
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            format.maximumFractionDigits = 0
            return format.format(amount).replace("Rp", "Rp ")
        }
    }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppNavTab
import com.example.ui.MawCookiesViewModel
import com.example.ui.components.MawBottomNavigationBar
import com.example.ui.components.MawHeaderBar
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.NotificationScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.ProductDetailSheet
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MawCookiesApp()
            }
        }
    }
}

@Composable
fun MawCookiesApp(
    viewModel: MawCookiesViewModel = viewModel()
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val selectedProduct by viewModel.selectedProduct.collectAsState()
    val cartItems by viewModel.cartItemsUi.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()
    val unreadNotifications by viewModel.unreadNotificationCount.collectAsState()

    val activeOrdersCount = allOrders.count { it.orderStatus != "DELIVERED" }
    val cartCount = cartItems.sumOf { it.quantity }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Modal Product Detail Bottom Sheet
    selectedProduct?.let { product ->
        val itemInCart = cartItems.find { it.product.id == product.id }
        ProductDetailSheet(
            product = product,
            initialQuantity = itemInCart?.quantity ?: 1,
            onAddToCart = { qty ->
                viewModel.addToCart(product.id, qty)
            },
            onDismiss = {
                viewModel.closeProductDetail()
            }
        )
    }

    // Route management
    when (currentScreen) {
        "CART" -> {
            CartScreen(viewModel = viewModel)
        }
        "CHECKOUT" -> {
            CheckoutScreen(viewModel = viewModel)
        }
        "PAYMENT_SIMULATION" -> {
            PaymentScreen(viewModel = viewModel)
        }
        "ORDER_TRACKING" -> {
            OrderTrackingScreen(viewModel = viewModel)
        }
        else -> {
            // Main Bottom Tabbed Container
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                topBar = {
                    if (currentTab == AppNavTab.KATALOG) {
                        MawHeaderBar(
                            cartCount = cartCount,
                            unreadNotificationCount = unreadNotifications,
                            onCartClick = { viewModel.openCart() },
                            onNotificationClick = { viewModel.setTab(AppNavTab.NOTIFIKASI) }
                        )
                    }
                },
                bottomBar = {
                    MawBottomNavigationBar(
                        currentTab = currentTab,
                        onTabSelected = { tab -> viewModel.setTab(tab) },
                        activeOrderCount = activeOrdersCount,
                        unreadNotificationCount = unreadNotifications
                    )
                },
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    AnimatedContent(
                        targetState = currentTab,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "tab_animation"
                    ) { tab ->
                        when (tab) {
                            AppNavTab.KATALOG -> CatalogScreen(viewModel = viewModel)
                            AppNavTab.PESANAN -> OrderTrackingScreen(viewModel = viewModel)
                            AppNavTab.NOTIFIKASI -> NotificationScreen(viewModel = viewModel)
                            AppNavTab.TENTANG -> AboutScreen()
                        }
                    }
                }
            }
        }
    }
}

package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CookieCatalog
import com.example.data.model.CookieProduct
import com.example.ui.CartItemUi
import com.example.ui.MawCookiesViewModel
import com.example.ui.components.CategorySelector
import com.example.ui.components.CookieProductCard
import com.example.ui.components.FlashDealsCountdownBar
import com.example.ui.components.HeroBanner
import com.example.ui.components.SearchBar
import com.example.ui.theme.CookieBackground
import com.example.ui.theme.CookiePrimary
import com.example.ui.theme.CookieSecondary
import com.example.ui.theme.CookieTextMuted
import com.example.ui.theme.CookieTextPrimary

@Composable
fun CatalogScreen(
    viewModel: MawCookiesViewModel,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val cartItems by viewModel.cartItemsUi.collectAsState()

    val filteredProducts = CookieCatalog.products.filter { product ->
        val matchesCategory = (selectedCategory == "Semua") || (product.category == selectedCategory)
        val matchesSearch = searchQuery.isBlank() ||
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.tagline.contains(searchQuery, ignoreCase = true) ||
                product.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    // Chunk products in pairs for 2-column grid inside LazyColumn
    val productChunks = filteredProducts.chunked(2)

    val cartTotalCount = cartItems.sumOf { it.quantity }
    val cartTotalPrice = viewModel.calculateSubtotal()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CookieBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = if (cartTotalCount > 0) 90.dp else 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Input
            item {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.setSearchQuery(it) }
                )
            }

            // Promotional Hero Banner (Hidden when actively searching)
            if (searchQuery.isBlank()) {
                item {
                    HeroBanner(
                        onShopNowClick = {
                            viewModel.setSelectedCategory("Signature Monster")
                        }
                    )
                }

                // Flash Deals Countdown Banner (Image 1 reference)
                item {
                    FlashDealsCountdownBar()
                }

                // Horizontal Category Selector
                item {
                    CategorySelector(
                        categories = CookieCatalog.categories,
                        selectedCategory = selectedCategory,
                        onCategorySelect = { viewModel.setSelectedCategory(it) }
                    )
                }
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (searchQuery.isNotBlank()) "Hasil Pencarian ('$searchQuery')" else "Menu Cookies Hari Ini",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookieTextPrimary
                    )
                    Text(
                        text = "${filteredProducts.size} varian",
                        fontSize = 12.sp,
                        color = CookieTextMuted
                    )
                }
            }

            // Empty search state
            if (filteredProducts.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🍪", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tidak menemukan cookies yang kamu cari",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CookieTextPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Coba cari dengan kata kunci lain seperti 'Oreo', 'Velvet', atau 'Choco'.",
                            fontSize = 12.sp,
                            color = CookieTextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // 2-Column Product Grid
            items(productChunks) { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Item 1
                    val p1 = pair[0]
                    val q1 = cartItems.find { it.product.id == p1.id }?.quantity ?: 0
                    CookieProductCard(
                        product = p1,
                        quantityInCart = q1,
                        onProductClick = { viewModel.openProductDetail(p1) },
                        onAddToCart = { viewModel.addToCart(p1.id, 1) },
                        onIncrement = { viewModel.addToCart(p1.id, 1) },
                        onDecrement = { viewModel.addToCart(p1.id, -1) },
                        modifier = Modifier.weight(1f)
                    )

                    // Item 2 (if exists)
                    if (pair.size > 1) {
                        val p2 = pair[1]
                        val q2 = cartItems.find { it.product.id == p2.id }?.quantity ?: 0
                        CookieProductCard(
                            product = p2,
                            quantityInCart = q2,
                            onProductClick = { viewModel.openProductDetail(p2) },
                            onAddToCart = { viewModel.addToCart(p2.id, 1) },
                            onIncrement = { viewModel.addToCart(p2.id, 1) },
                            onDecrement = { viewModel.addToCart(p2.id, -1) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Floating Bottom Sticky Cart Bar when items are in cart
        if (cartTotalCount > 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(12.dp, RoundedCornerShape(16.dp))
                    .clickable { viewModel.openCart() }
                    .testTag("floating_cart_bar"),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF1B5E20)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocalMall,
                                    contentDescription = "Cart",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "$cartTotalCount Cookies di Keranjang",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = MawCookiesViewModel.formatRupiah(cartTotalPrice),
                                fontSize = 12.sp,
                                color = Color(0xFFC8E6C9)
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.openCart() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF5A623),
                            contentColor = Color(0xFF2E190A)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Lihat Keranjang →", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CookieProduct
import com.example.ui.AppNavTab
import com.example.ui.MawCookiesViewModel
import com.example.ui.theme.CookieBackground
import com.example.ui.theme.CookieDiscountBadge
import com.example.ui.theme.CookiePillGreen
import com.example.ui.theme.CookiePillGreenText
import com.example.ui.theme.CookiePrimary
import com.example.ui.theme.CookieSecondary
import com.example.ui.theme.CookieTextMuted
import com.example.ui.theme.CookieTextPrimary
import com.example.ui.theme.CookieTextSecondary
import kotlinx.coroutines.delay

@Composable
fun MawHeaderBar(
    cartCount: Int,
    unreadNotificationCount: Int,
    onCartClick: () -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Top Announcement Strip (like Image 1 "Order now and get it within 15-30 mins")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE8F5E9))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "🍪 Maw Cookies Kitchen",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            }
            Text(
                text = "Panggang Baru • Kirim 30 Mnt ⚡",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2E7D32)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Brand & Action Icons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Maw Cookies",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2B170B),
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Artisanal Soft-Baked & Chunky Cookies",
                    fontSize = 11.sp,
                    color = CookieTextSecondary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Notification Button with badge
                BadgedBox(
                    badge = {
                        if (unreadNotificationCount > 0) {
                            Badge(
                                containerColor = CookieDiscountBadge,
                                contentColor = Color.White
                            ) {
                                Text(unreadNotificationCount.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF7F4EF))
                            .testTag("notification_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = Color(0xFF4A3525),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Cart Button with badge
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(
                                containerColor = CookiePrimary,
                                contentColor = Color.White
                            ) {
                                Text(cartCount.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = onCartClick,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CookiePillGreen)
                            .testTag("cart_header_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalMall,
                            contentDescription = "Keranjang",
                            tint = CookiePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                "Cari cookies favoritmu (Oreo, Red Velvet, Matcha...)",
                fontSize = 13.sp,
                color = CookieTextMuted
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF756557),
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color(0xFF756557),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CookiePrimary,
            unfocusedBorderColor = Color(0xFFE6DEC3),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color(0xFFFAF7F0)
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("search_cookie_input")
    )
}

@Composable
fun HeroBanner(
    onShopNowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF00583A),
                            Color(0xFF007A50),
                            Color(0xFF1B6B48)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1.3f)) {
                    Surface(
                        color = Color(0xFFFDD835),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "FRESH FROM THE OVEN",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF261D0C),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Make life sweeter\nwith fresh cookies.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "100% Mentega New Zealand & Belgian Choco.",
                        fontSize = 11.sp,
                        color = Color(0xFFD4EBD9)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onShopNowClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF5A623),
                            contentColor = Color(0xFF2E190A)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("hero_shop_now_button")
                    ) {
                        Text("Beli Sekarang →", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Decorative Cookie Stack Illustration
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .height(105.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cookie_monster),
                        contentDescription = "Maw Cookies Banner",
                        modifier = Modifier.size(90.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
fun FlashDealsCountdownBar(
    modifier: Modifier = Modifier
) {
    var seconds by remember { mutableIntStateOf(48) }
    var minutes by remember { mutableIntStateOf(14) }
    var hours by remember { mutableIntStateOf(4) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            if (seconds > 0) {
                seconds--
            } else {
                seconds = 59
                if (minutes > 0) {
                    minutes--
                } else {
                    minutes = 59
                    if (hours > 0) hours--
                }
            }
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFFF3E0))
            .border(1.dp, Color(0xFFFFCC80), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "⚡ Flash Promo Hari Ini",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD84315)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
                color = Color(0xFFD84315),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Diskon s/d 25%",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        }

        // Timer badge: 04 : 14 : 48
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Berakhir: ",
                fontSize = 10.sp,
                color = Color(0xFF8D6E63)
            )
            TimeBadge(String.format("%02d", hours))
            Text(":", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD84315))
            TimeBadge(String.format("%02d", minutes))
            Text(":", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD84315))
            TimeBadge(String.format("%02d", seconds))
        }
    }
}

@Composable
private fun TimeBadge(value: String) {
    Surface(
        color = Color(0xFF3E1E0F),
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun CategorySelector(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Kategori Pilihan",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = CookieTextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Surface(
                    onClick = { onCategorySelect(category) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) CookiePrimary else Color.White,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) CookiePrimary else Color(0xFFE2D9CC)
                    ),
                    modifier = Modifier.testTag("category_chip_$category")
                ) {
                    Text(
                        text = category,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else CookieTextSecondary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CookieProductCard(
    product: CookieProduct,
    quantityInCart: Int,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onProductClick)
            .border(1.dp, Color(0xFFF0EBE1), RoundedCornerShape(14.dp))
            .testTag("cookie_card_${product.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Cookie Visual Container with Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFBF8F3)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = product.drawableResId),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(90.dp)
                        .padding(4.dp),
                    contentScale = ContentScale.Fit
                )

                // Badge top-left (e.g. Best Seller, Signature)
                if (product.badge != null) {
                    Surface(
                        color = when (product.badge) {
                            "Best Seller" -> Color(0xFF007A50)
                            "Signature" -> Color(0xFFC62828)
                            "Hemat 15%" -> Color(0xFFD84315)
                            else -> Color(0xFF4E2718)
                        },
                        shape = RoundedCornerShape(bottomEnd = 8.dp, topStart = 10.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = product.badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Weight Tag bottom-right
                Surface(
                    color = Color(0xCCFFFFFF),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "${product.weightGrams}g",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6B5849),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CookieTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Tagline / Subtitle
            Text(
                text = product.tagline,
                fontSize = 11.sp,
                color = CookieTextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Rating & Price row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⭐ ${product.rating}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                    Text(text = " (${product.reviewsCount})", fontSize = 9.sp, color = CookieTextMuted)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Price & Add Button Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    if (product.originalPrice != null) {
                        Text(
                            text = MawCookiesViewModel.formatRupiah(product.originalPrice),
                            fontSize = 10.sp,
                            color = CookieTextMuted,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                    Text(
                        text = MawCookiesViewModel.formatRupiah(product.price),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CookiePrimary
                    )
                }

                // Image 1 Style Button:
                // If qty == 0: '+' button pill
                // If qty > 0: '[-] [qty] [+]' pill in green!
                if (quantityInCart == 0) {
                    Surface(
                        onClick = onAddToCart,
                        shape = RoundedCornerShape(8.dp),
                        color = CookiePillGreen,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9)),
                        modifier = Modifier.testTag("add_to_cart_${product.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Tambah",
                                tint = CookiePrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    // Expanded quantity counter pill matching Image 1
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CookiePrimary,
                        modifier = Modifier.testTag("qty_pill_${product.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clickable(onClick = onDecrement),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Kurang",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            Text(
                                text = quantityInCart.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clickable(onClick = onIncrement),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Tambah",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MawBottomNavigationBar(
    currentTab: AppNavTab,
    onTabSelected: (AppNavTab) -> Unit,
    activeOrderCount: Int,
    unreadNotificationCount: Int,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = CookiePrimary,
        tonalElevation = 6.dp,
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentTab == AppNavTab.KATALOG,
            onClick = { onTabSelected(AppNavTab.KATALOG) },
            icon = {
                Icon(
                    imageVector = if (currentTab == AppNavTab.KATALOG) Icons.Filled.Storefront else Icons.Outlined.Storefront,
                    contentDescription = "Katalog"
                )
            },
            label = { Text("Katalog", fontSize = 11.sp, fontWeight = if (currentTab == AppNavTab.KATALOG) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CookiePrimary,
                selectedTextColor = CookiePrimary,
                indicatorColor = CookiePillGreen
            ),
            modifier = Modifier.testTag("nav_katalog")
        )

        NavigationBarItem(
            selected = currentTab == AppNavTab.PESANAN,
            onClick = { onTabSelected(AppNavTab.PESANAN) },
            icon = {
                BadgedBox(
                    badge = {
                        if (activeOrderCount > 0) {
                            Badge(containerColor = CookieSecondary, contentColor = Color.White) {
                                Text(activeOrderCount.toString(), fontSize = 9.sp)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == AppNavTab.PESANAN) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                        contentDescription = "Pesanan"
                    )
                }
            },
            label = { Text("Pesanan", fontSize = 11.sp, fontWeight = if (currentTab == AppNavTab.PESANAN) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CookiePrimary,
                selectedTextColor = CookiePrimary,
                indicatorColor = CookiePillGreen
            ),
            modifier = Modifier.testTag("nav_pesanan")
        )

        NavigationBarItem(
            selected = currentTab == AppNavTab.NOTIFIKASI,
            onClick = { onTabSelected(AppNavTab.NOTIFIKASI) },
            icon = {
                BadgedBox(
                    badge = {
                        if (unreadNotificationCount > 0) {
                            Badge(containerColor = CookieDiscountBadge, contentColor = Color.White) {
                                Text(unreadNotificationCount.toString(), fontSize = 9.sp)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentTab == AppNavTab.NOTIFIKASI) Icons.Filled.Notifications else Icons.Outlined.Notifications,
                        contentDescription = "Notifikasi"
                    )
                }
            },
            label = { Text("Notifikasi", fontSize = 11.sp, fontWeight = if (currentTab == AppNavTab.NOTIFIKASI) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CookiePrimary,
                selectedTextColor = CookiePrimary,
                indicatorColor = CookiePillGreen
            ),
            modifier = Modifier.testTag("nav_notifikasi")
        )

        NavigationBarItem(
            selected = currentTab == AppNavTab.TENTANG,
            onClick = { onTabSelected(AppNavTab.TENTANG) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Tentang"
                )
            },
            label = { Text("Tentang", fontSize = 11.sp, fontWeight = if (currentTab == AppNavTab.TENTANG) FontWeight.Bold else FontWeight.Normal) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CookiePrimary,
                selectedTextColor = CookiePrimary,
                indicatorColor = CookiePillGreen
            ),
            modifier = Modifier.testTag("nav_tentang")
        )
    }
}

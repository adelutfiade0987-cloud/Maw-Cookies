package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CartItemUi
import com.example.ui.MawCookiesViewModel
import com.example.ui.theme.CookieBackground
import com.example.ui.theme.CookieDiscountBadge
import com.example.ui.theme.CookiePillGreen
import com.example.ui.theme.CookiePrimary
import com.example.ui.theme.CookieTextMuted
import com.example.ui.theme.CookieTextPrimary
import com.example.ui.theme.CookieTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: MawCookiesViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val cartItems by viewModel.cartItemsUi.collectAsState()
    val appliedPromo by viewModel.appliedPromo.collectAsState()
    val promoInput by viewModel.promoInput.collectAsState()

    val subtotal = viewModel.calculateSubtotal()
    val deliveryFee = viewModel.calculateDeliveryFee()
    val discount = viewModel.calculateDiscount()
    val total = viewModel.calculateTotal()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Keranjang Belanja (${cartItems.sumOf { it.quantity }})",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookieTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = CookieTextPrimary
                        )
                    }
                },
                actions = {
                    if (cartItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearCart() }) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Kosongkan Keranjang",
                                tint = Color(0xFFC62828)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Pembayaran", fontSize = 12.sp, color = CookieTextSecondary)
                                Text(
                                    text = MawCookiesViewModel.formatRupiah(total),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = CookiePrimary
                                )
                            }

                            Button(
                                onClick = { viewModel.openCheckout() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CookiePrimary,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("checkout_continue_button")
                            ) {
                                Text("Lanjut Checkout →", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(CookieBackground),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(text = "🛒", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Keranjangmu masih kosong!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookieTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Yuk pilih varian cookies lezat Maw Cookies yang baru dipanggang hari ini.",
                        fontSize = 13.sp,
                        color = CookieTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.navigateBack() },
                        colors = ButtonDefaults.buttonColors(containerColor = CookiePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Mulai Belanja 🍪", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(CookieBackground),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Cart Items List
                items(cartItems) { item ->
                    CartItemCard(
                        item = item,
                        onIncrement = { viewModel.addToCart(item.product.id, 1) },
                        onDecrement = { viewModel.addToCart(item.product.id, -1) },
                        onRemove = { viewModel.removeFromCart(item.product.id) }
                    )
                }

                // Promo Voucher Section
                item {
                    PromoCodeCard(
                        promoInput = promoInput,
                        appliedPromo = appliedPromo,
                        availablePromos = viewModel.availablePromos,
                        onInputChange = { viewModel.promoInput.value = it },
                        onApply = { viewModel.applyPromo(it) },
                        onRemove = { viewModel.removePromo() }
                    )
                }

                // Order Price Breakdown Card
                item {
                    PriceBreakdownCard(
                        subtotal = subtotal,
                        deliveryFee = deliveryFee,
                        discount = discount,
                        total = total,
                        promoCode = appliedPromo?.code
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: CartItemUi,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
            .testTag("cart_item_${item.product.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFBF8F3)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.product.drawableResId),
                    contentDescription = item.product.name,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CookieTextPrimary
                )
                Text(
                    text = "${item.product.weightGrams}g • ${MawCookiesViewModel.formatRupiah(item.product.price)}",
                    fontSize = 11.sp,
                    color = CookieTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = MawCookiesViewModel.formatRupiah(item.product.price * item.quantity),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CookiePrimary
                )
            }

            // Stepper Pill
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF5EFE6)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable(onClick = onDecrement),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Kurang",
                            tint = Color(0xFF332014),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Text(
                        text = item.quantity.toString(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF332014),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable(onClick = onIncrement),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah",
                            tint = Color(0xFF332014),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Hapus",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun PromoCodeCard(
    promoInput: String,
    appliedPromo: com.example.ui.PromoOption?,
    availablePromos: List<com.example.ui.PromoOption>,
    onInputChange: (String) -> Unit,
    onApply: (String) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = "Promo",
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Voucher Diskon Maw Cookies",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CookieTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (appliedPromo != null) {
                // Applied State
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFA5D6A7)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Applied",
                                tint = Color(0xFF1B5E20),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Voucher: ${appliedPromo.code}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                                Text(
                                    text = appliedPromo.description,
                                    fontSize = 10.sp,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }

                        IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus Promo",
                                tint = Color(0xFF1B5E20),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            } else {
                // Input form
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = promoInput,
                        onValueChange = onInputChange,
                        placeholder = { Text("Contoh: MAWCHUNKY", fontSize = 12.sp) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CookiePrimary,
                            unfocusedBorderColor = Color(0xFFDED5C8)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("promo_code_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onApply(promoInput) },
                        enabled = promoInput.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = CookiePrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("apply_promo_button")
                    ) {
                        Text("Pasang", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quick tap coupons
                Text("Pilih kupon cepat:", fontSize = 11.sp, color = CookieTextMuted)
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(availablePromos) { promo ->
                        Surface(
                            onClick = { onApply(promo.code) },
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFFF3E0),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCC80))
                        ) {
                            Text(
                                text = "🏷️ ${promo.code}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD84315),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PriceBreakdownCard(
    subtotal: Int,
    deliveryFee: Int,
    discount: Int,
    total: Int,
    promoCode: String?,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ringkasan Pembayaran",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CookieTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            PriceRow(label = "Total Harga Cookies", value = MawCookiesViewModel.formatRupiah(subtotal))
            Spacer(modifier = Modifier.height(6.dp))
            PriceRow(label = "Ongkos Kirim", value = if (deliveryFee == 0) "GRATIS" else MawCookiesViewModel.formatRupiah(deliveryFee))

            if (discount > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                PriceRow(
                    label = "Diskon Voucher (${promoCode ?: ""})",
                    value = "- " + MawCookiesViewModel.formatRupiah(discount),
                    valueColor = Color(0xFF007A50)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            PriceRow(label = "Biaya Layanan & Box Kemasan", value = "GRATIS (Rp 0)")

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF2ECE1))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Tagihan",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = CookieTextPrimary
                )
                Text(
                    text = MawCookiesViewModel.formatRupiah(total),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CookiePrimary
                )
            }
        }
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String,
    valueColor: Color = CookieTextPrimary
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = CookieTextSecondary)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = valueColor)
    }
}

package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MawCookiesViewModel
import com.example.ui.theme.CookieBackground
import com.example.ui.theme.CookiePrimary
import com.example.ui.theme.CookieSecondary
import com.example.ui.theme.CookieTextMuted
import com.example.ui.theme.CookieTextPrimary
import com.example.ui.theme.CookieTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: MawCookiesViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val customerName by viewModel.customerName.collectAsState()
    val customerPhone by viewModel.customerPhone.collectAsState()
    val customerAddress by viewModel.customerAddress.collectAsState()
    val customerDeliveryNote by viewModel.customerDeliveryNote.collectAsState()
    val selectedDeliveryMethod by viewModel.selectedDeliveryMethod.collectAsState()
    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsState()
    val cartItems by viewModel.cartItemsUi.collectAsState()

    val total = viewModel.calculateTotal()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Konfirmasi Pesanan",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total Tagihan", fontSize = 12.sp, color = CookieTextSecondary)
                        Text(
                            text = MawCookiesViewModel.formatRupiah(total),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = CookiePrimary
                        )
                    }

                    Button(
                        onClick = { viewModel.processCheckout() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CookiePrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("submit_order_button")
                    ) {
                        Text("Bayar Sekarang →", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(CookieBackground),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Customer Info Section
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = CookiePrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Informasi Penerima", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = customerName,
                            onValueChange = { viewModel.customerName.value = it },
                            label = { Text("Nama Lengkap", fontSize = 12.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("input_customer_name")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = customerPhone,
                            onValueChange = { viewModel.customerPhone.value = it },
                            label = { Text("Nomor WhatsApp / HP", fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp)) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("input_customer_phone")
                        )
                    }
                }
            }

            // Delivery Address Section
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = CookieSecondary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Alamat Pengantaran Cookies", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = customerAddress,
                            onValueChange = { viewModel.customerAddress.value = it },
                            label = { Text("Alamat Lengkap", fontSize = 12.sp) },
                            shape = RoundedCornerShape(10.dp),
                            minLines = 2,
                            maxLines = 3,
                            modifier = Modifier.fillMaxWidth().testTag("input_customer_address")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = customerDeliveryNote,
                            onValueChange = { viewModel.customerDeliveryNote.value = it },
                            label = { Text("Catatan untuk Kurir (Opsional)", fontSize = 12.sp) },
                            placeholder = { Text("Contoh: Titip di satpam / bel pagar 2x", fontSize = 11.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth().testTag("input_customer_note")
                        )
                    }
                }
            }

            // Delivery Option Section
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DeliveryDining, contentDescription = null, tint = CookiePrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pilihan Pengiriman", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        DeliveryOptionItem(
                            title = "Kurir Instan (30-45 Menit)",
                            desc = "Cookies diantar langsung selagi hangat dari oven",
                            price = "Rp 15.000",
                            selected = selectedDeliveryMethod == "Kurir Instan (30-45 Menit)",
                            onSelect = { viewModel.selectedDeliveryMethod.value = "Kurir Instan (30-45 Menit)" }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DeliveryOptionItem(
                            title = "Same Day Delivery (2-4 Jam)",
                            desc = "Pengantaran terjadwal hemat biaya",
                            price = "Rp 10.000",
                            selected = selectedDeliveryMethod == "Same Day Delivery (2-4 Jam)",
                            onSelect = { viewModel.selectedDeliveryMethod.value = "Same Day Delivery (2-4 Jam)" }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        DeliveryOptionItem(
                            title = "Ambil di Toko (Pickup Fresh)",
                            desc = "Ambil langsung di Kitchen Maw Cookies",
                            price = "GRATIS",
                            selected = selectedDeliveryMethod == "Ambil di Toko (Pickup Fresh)",
                            onSelect = { viewModel.selectedDeliveryMethod.value = "Ambil di Toko (Pickup Fresh)" }
                        )
                    }
                }
            }

            // Online Payment Method Section
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Metode Pembayaran Online",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CookieTextPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        PaymentMethodItem(
                            icon = Icons.Default.QrCode2,
                            title = "QRIS (Semua E-Wallet & Bank)",
                            desc = "BCA, GoPay, OVO, ShopeePay, DANA, LinkAja",
                            isPopular = true,
                            selected = selectedPaymentMethod == "QRIS",
                            onSelect = { viewModel.selectedPaymentMethod.value = "QRIS" }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PaymentMethodItem(
                            icon = Icons.Default.AccountBalance,
                            title = "BCA Virtual Account",
                            desc = "Verifikasi otomatis 24 jam",
                            selected = selectedPaymentMethod == "BCA Virtual Account",
                            onSelect = { viewModel.selectedPaymentMethod.value = "BCA Virtual Account" }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PaymentMethodItem(
                            icon = Icons.Default.AccountBalance,
                            title = "Mandiri Virtual Account",
                            desc = "Verifikasi otomatis 24 jam",
                            selected = selectedPaymentMethod == "Mandiri Virtual Account",
                            onSelect = { viewModel.selectedPaymentMethod.value = "Mandiri Virtual Account" }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PaymentMethodItem(
                            icon = Icons.Default.CreditCard,
                            title = "GoPay / ShopeePay / DANA",
                            desc = "Bayar instan via e-wallet",
                            selected = selectedPaymentMethod == "GoPay",
                            onSelect = { viewModel.selectedPaymentMethod.value = "GoPay" }
                        )
                    }
                }
            }

            // Order Items Review summary
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Pesanan Cookies (${cartItems.size} jenis):", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        cartItems.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${item.quantity}x ${item.product.name}", fontSize = 12.sp, color = CookieTextSecondary)
                                Text(MawCookiesViewModel.formatRupiah(item.product.price * item.quantity), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeliveryOptionItem(
    title: String,
    desc: String,
    price: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Color(0xFFF1F8F4) else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (selected) CookiePrimary else Color(0xFFE5DDD0)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = CookiePrimary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                Text(desc, fontSize = 11.sp, color = CookieTextSecondary)
            }
            Text(price, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selected) CookiePrimary else CookieTextPrimary)
        }
    }
}

@Composable
private fun PaymentMethodItem(
    icon: ImageVector,
    title: String,
    desc: String,
    isPopular: Boolean = false,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(10.dp),
        color = if (selected) Color(0xFFF1F8F4) else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (selected) CookiePrimary else Color(0xFFE5DDD0)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = CookiePrimary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = icon, contentDescription = null, tint = if (selected) CookiePrimary else Color(0xFF5A4434), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                    if (isPopular) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text("Rekomendasi", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20), modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                        }
                    }
                }
                Text(desc, fontSize = 11.sp, color = CookieTextSecondary)
            }
        }
    }
}

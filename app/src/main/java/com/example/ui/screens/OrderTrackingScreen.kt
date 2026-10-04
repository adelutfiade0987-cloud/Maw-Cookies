package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.OrderEntity
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(
    viewModel: MawCookiesViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val context = LocalContext.current
    val activeTrackingId by viewModel.activeTrackingOrderId.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()

    // Determine target order to view
    val targetOrder = allOrders.find { it.orderId == activeTrackingId } ?: allOrders.firstOrNull()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (targetOrder != null) "Pelacakan Pesanan ${targetOrder.orderId}" else "Daftar Pesanan",
                        fontSize = 16.sp,
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
                    if (targetOrder != null && targetOrder.orderStatus != "DELIVERED") {
                        // Quick simulation button
                        IconButton(
                            onClick = {
                                viewModel.advanceOrderStatus(targetOrder.orderId)
                            },
                            modifier = Modifier.testTag("advance_status_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.FastForward,
                                contentDescription = "Percepat Status",
                                tint = CookiePrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        modifier = modifier
    ) { paddingValues ->
        if (targetOrder == null) {
            // No orders yet
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
                    Text(text = "📦", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Belum ada pesanan aktif",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookieTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Cookies lezatmu akan muncul di sini setelah kamu melakukan pemesanan.",
                        fontSize = 13.sp,
                        color = CookieTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.setTab(com.example.ui.AppNavTab.KATALOG) },
                        colors = ButtonDefaults.buttonColors(containerColor = CookiePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Buka Katalog Cookies", fontWeight = FontWeight.Bold)
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Live Status Hero Banner
                item {
                    LiveStatusCard(
                        order = targetOrder,
                        onSimulateNext = { viewModel.advanceOrderStatus(targetOrder.orderId) }
                    )
                }

                // Interactive Progress Stepper
                item {
                    OrderStepperCard(status = targetOrder.orderStatus)
                }

                // Courier Details Card (Visible when baking, packaging, or delivering)
                if (targetOrder.orderStatus in listOf("BAKING", "PACKAGING", "DELIVERING", "DELIVERED")) {
                    item {
                        CourierInfoCard(
                            order = targetOrder,
                            onCall = {
                                Toast.makeText(context, "Menghubungi ${targetOrder.courierName}...", Toast.LENGTH_SHORT).show()
                            },
                            onChat = {
                                Toast.makeText(context, "Membuka obrolan kurir...", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }

                // Order Details Breakdown
                item {
                    OrderDetailSummaryCard(order = targetOrder)
                }

                // Past Orders Selector if there are multiple orders
                if (allOrders.size > 1) {
                    item {
                        Text(
                            text = "Riwayat Pesanan Lainnya",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CookieTextPrimary
                        )
                    }

                    items(allOrders.filter { it.orderId != targetOrder.orderId }) { pastOrder ->
                        PastOrderItemCard(
                            order = pastOrder,
                            onClick = { viewModel.openTracking(pastOrder.orderId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveStatusCard(
    order: OrderEntity,
    onSimulateNext: () -> Unit
) {
    val (statusLabel, statusDesc, progressValue, iconRes) = when (order.orderStatus) {
        "WAITING_PAYMENT" -> Quadruple(
            "Menunggu Pembayaran",
            "Selesaikan pembayaran agar pesananmu segera diproses dapur.",
            0.15f,
            R.drawable.ic_qris_qr
        )
        "CONFIRMED" -> Quadruple(
            "Pesanan Dikonfirmasi ✅",
            "Dapur Maw Cookies telah menerima tiket dan menyiapkan bahan terbaik.",
            0.35f,
            R.drawable.ic_cookie_monster
        )
        "BAKING" -> Quadruple(
            "Sedang Dipanggang di Oven ♨️",
            "Aroma harum semerbak! Cookies segarmu sedang dipanggang hangat.",
            0.55f,
            R.drawable.ic_oven_warm
        )
        "PACKAGING" -> Quadruple(
            "Pendinginan & Pengemasan 📦",
            "Cookies baru matang dimasukkan ke box eksklusif Maw Cookies.",
            0.75f,
            R.drawable.ic_cookie_box
        )
        "DELIVERING" -> Quadruple(
            "Kurir Sedang Mengantar 🛵",
            "Kurir ${order.courierName} sedang melaju menuju lokasimu!",
            0.90f,
            R.drawable.ic_delivery_bike
        )
        "DELIVERED" -> Quadruple(
            "Pesanan Telah Tiba! 🎉",
            "Cookies telah berhasil diantarkan. Nikmati selagi hangat bersama susu!",
            1.0f,
            R.drawable.ic_cookie_milk
        )
        else -> Quadruple("Memproses Pesanan", "Pesanan sedang diproses.", 0.2f, R.drawable.ic_cookie_monster)
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF9F5EC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = statusLabel,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CookieTextPrimary
                        )
                        Text(
                            text = if (order.orderStatus == "DELIVERED") "Tiba di tujuan" else "Estimasi sampai: ~20-30 Menit",
                            fontSize = 11.sp,
                            color = CookiePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = statusDesc, fontSize = 12.sp, color = CookieTextSecondary, lineHeight = 16.sp)

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progressValue },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = CookiePrimary,
                trackColor = Color(0xFFE8F5E9)
            )

            if (order.orderStatus != "DELIVERED") {
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedButton(
                    onClick = onSimulateNext,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CookiePrimary),
                    modifier = Modifier.fillMaxWidth().height(38.dp)
                ) {
                    Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Percepat Status (Simulasi Real-Time)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun OrderStepperCard(status: String) {
    val steps = listOf(
        Pair("CONFIRMED", "Pesanan Diterima & Masuk Antrian Dapur"),
        Pair("BAKING", "Dipanggang Hangat di Oven"),
        Pair("PACKAGING", "Pengemasan Box & Quality Check"),
        Pair("DELIVERING", "Diambil Kurir & Sedang Diantar"),
        Pair("DELIVERED", "Tiba di Lokasi & Selesai")
    )

    val currentStepIndex = when (status) {
        "WAITING_PAYMENT" -> -1
        "CONFIRMED" -> 0
        "BAKING" -> 1
        "PACKAGING" -> 2
        "DELIVERING" -> 3
        "DELIVERED" -> 4
        else -> 0
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tahapan Proses Pesanan",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CookieTextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            steps.forEachIndexed { index, pair ->
                val isCompleted = index <= currentStepIndex
                val isCurrent = index == currentStepIndex

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circle indicator
                    Surface(
                        shape = CircleShape,
                        color = when {
                            isCompleted -> CookiePrimary
                            else -> Color(0xFFE0E0E0)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isCompleted) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            } else {
                                Text((index + 1).toString(), fontSize = 10.sp, color = Color(0xFF757575), fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = pair.second,
                            fontSize = 13.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCompleted) CookieTextPrimary else CookieTextMuted
                        )
                        if (isCurrent) {
                            Text(
                                text = "Sedang berlangsung saat ini",
                                fontSize = 10.sp,
                                color = CookiePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                if (index < steps.lastIndex) {
                    Box(
                        modifier = Modifier
                            .padding(start = 11.dp)
                            .width(2.dp)
                            .height(20.dp)
                            .background(if (index < currentStepIndex) CookiePrimary else Color(0xFFE0E0E0))
                    )
                }
            }
        }
    }
}

@Composable
private fun CourierInfoCard(
    order: OrderEntity,
    onCall: () -> Unit,
    onChat: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Kurir Pengantar Maw Cookies",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CookieTextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_delivery_bike),
                            contentDescription = "Kurir",
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(order.courierName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                        Text(order.courierPlate, fontSize = 11.sp, color = CookieTextSecondary)
                        Text("⭐ 4.9 (1.200+ Pesanan Antar)", fontSize = 10.sp, color = Color(0xFFE65100))
                    }
                }

                Row {
                    IconButton(
                        onClick = onCall,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9))
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Telepon", tint = CookiePrimary, modifier = Modifier.size(18.dp))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onChat,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE8F5E9))
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = "Chat", tint = CookiePrimary, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderDetailSummaryCard(order: OrderEntity) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Rincian Pesanan",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CookieTextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Item Cookies", fontSize = 12.sp, color = CookieTextSecondary)
                Text(order.itemsSummary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CookieTextPrimary)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Alamat Pengiriman", fontSize = 12.sp, color = CookieTextSecondary)
                Text(order.deliveryAddress, fontSize = 11.sp, color = CookieTextPrimary, modifier = Modifier.width(200.dp), textAlign = TextAlign.End)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Metode Pembayaran", fontSize = 12.sp, color = CookieTextSecondary)
                Text("${order.paymentMethod} (${order.paymentStatus})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CookiePrimary)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF2ECE1))
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total Dibayar", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                Text(MawCookiesViewModel.formatRupiah(order.totalAmount), fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = CookiePrimary)
            }
        }
    }
}

@Composable
private fun PastOrderItemCard(
    order: OrderEntity,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF2ECE1)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Pesanan ${order.orderId}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                Text(order.itemsSummary, fontSize = 11.sp, color = CookieTextSecondary)
                Text(MawCookiesViewModel.formatRupiah(order.totalAmount), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CookiePrimary)
            }

            Surface(
                color = if (order.orderStatus == "DELIVERED") Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = if (order.orderStatus == "DELIVERED") "Selesai" else "Dalam Proses",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (order.orderStatus == "DELIVERED") Color(0xFF1B5E20) else Color(0xFFD84315),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

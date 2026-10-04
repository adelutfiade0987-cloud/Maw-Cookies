package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.entity.OrderEntity
import com.example.ui.MawCookiesViewModel
import com.example.ui.theme.CookieBackground
import com.example.ui.theme.CookiePrimary
import com.example.ui.theme.CookieSecondary
import com.example.ui.theme.CookieTextMuted
import com.example.ui.theme.CookieTextPrimary
import com.example.ui.theme.CookieTextSecondary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    viewModel: MawCookiesViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateBack()
    }

    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val activeOrderId by viewModel.activeTrackingOrderId.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()

    val currentOrder = allOrders.find { it.orderId == activeOrderId }

    var isVerifying by remember { mutableStateOf(false) }
    var countdownSeconds by remember { mutableIntStateOf(14 * 60 + 58) }

    LaunchedEffect(Unit) {
        while (countdownSeconds > 0) {
            delay(1000)
            countdownSeconds--
        }
    }

    val minutes = countdownSeconds / 60
    val seconds = countdownSeconds % 60
    val timerString = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pembayaran Online",
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
                Column(modifier = Modifier.padding(16.dp)) {
                    Button(
                        onClick = {
                            if (!isVerifying && currentOrder != null) {
                                isVerifying = true
                                viewModel.confirmPayment(currentOrder.orderId)
                            }
                        },
                        enabled = !isVerifying,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CookiePrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("verify_payment_simulation_button")
                    ) {
                        if (isVerifying) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Memverifikasi Transaksi...", fontSize = 13.sp)
                        } else {
                            Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Simulasi: Bayar Sekarang (Berhasil)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        modifier = modifier
    ) { paddingValues ->
        if (currentOrder == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = CookiePrimary)
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
                // Countdown Timer Header
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCC80)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Timer, contentDescription = null, tint = Color(0xFFD84315), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Selesaikan pembayaran dalam",
                                    fontSize = 12.sp,
                                    color = Color(0xFF5D4037)
                                )
                            }

                            Text(
                                text = timerString,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFD84315),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // Invoice Summary Box
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Nomor Pesanan", fontSize = 11.sp, color = CookieTextSecondary)
                                    Text(currentOrder.orderId, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                                }
                                Surface(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = currentOrder.paymentMethod,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color(0xFFF2ECE1))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text("Total yang Harus Dibayar", fontSize = 12.sp, color = CookieTextSecondary)
                            Text(
                                text = MawCookiesViewModel.formatRupiah(currentOrder.totalAmount),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CookiePrimary
                            )
                        }
                    }
                }

                // Payment Channel Specific UI (QRIS / Virtual Account)
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
                    ) {
                        if (currentOrder.paymentMethod == "QRIS") {
                            // QRIS Code display
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Scan QRIS Maw Cookies",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CookieTextPrimary
                                )
                                Text(
                                    text = "NMID: ID1020261004128 • Maw Cookies Kitchen",
                                    fontSize = 11.sp,
                                    color = CookieTextSecondary
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Real QR Box
                                Box(
                                    modifier = Modifier
                                        .size(200.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(Color.White)
                                        .border(2.dp, Color(0xFF263238), RoundedCornerShape(14.dp))
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_qris_qr),
                                        contentDescription = "QRIS QR Code",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            Toast.makeText(context, "Kode QRIS berhasil diunduh!", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Simpan Gambar QRIS", fontSize = 11.sp)
                                    }
                                }
                            }
                        } else {
                            // Virtual Account display
                            Column(modifier = Modifier.padding(16.dp)) {
                                val vaNumber = if (currentOrder.paymentMethod.contains("Mandiri")) {
                                    "8932 0812 8765 4321"
                                } else {
                                    "8271 0812 8765 4321"
                                }

                                Text(
                                    text = "Nomor Virtual Account",
                                    fontSize = 13.sp,
                                    color = CookieTextSecondary
                                )
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xFFF9F6F0))
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = vaNumber,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = CookieTextPrimary
                                    )

                                    IconButton(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(vaNumber.replace(" ", "")))
                                            Toast.makeText(context, "Nomor VA disalin!", Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(Icons.Default.ContentCopy, contentDescription = "Salin", tint = CookiePrimary)
                                    }
                                }
                            }
                        }
                    }
                }

                // Payment Guide Accordion Steps
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Petunjuk Pembayaran",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CookieTextPrimary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            StepInstructionItem(number = "1", text = "Buka aplikasi e-Wallet (GoPay, OVO, ShopeePay, DANA) atau Mobile Banking BCA/Mandiri/BRI.")
                            StepInstructionItem(number = "2", text = "Pilih menu QRIS (Scan) atau menu Transfer Virtual Account.")
                            StepInstructionItem(number = "3", text = "Periksa nama penerima: Maw Cookies Kitchen dan nominal tagihan.")
                            StepInstructionItem(number = "4", text = "Masukkan PIN transaksi Anda untuk menyelesaikan pembayaran.")
                            StepInstructionItem(number = "5", text = "Tekan tombol konfirmasi di bawah untuk memverifikasi pesanan secara otomatis.")
                        }
                    }
                }

                // Security Banner
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF6B5849), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pembayaran aman & terenkripsi otomatis 256-bit SSL",
                            fontSize = 11.sp,
                            color = CookieTextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepInstructionItem(number: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            color = Color(0xFFE8F5E9),
            shape = CircleShape,
            modifier = Modifier.size(20.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = number, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, fontSize = 12.sp, color = CookieTextSecondary, lineHeight = 16.sp)
    }
}

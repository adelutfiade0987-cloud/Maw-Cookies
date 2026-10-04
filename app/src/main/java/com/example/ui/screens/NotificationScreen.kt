package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.NotificationEntity
import com.example.ui.MawCookiesViewModel
import com.example.ui.theme.CookieBackground
import com.example.ui.theme.CookiePrimary
import com.example.ui.theme.CookieTextMuted
import com.example.ui.theme.CookieTextPrimary
import com.example.ui.theme.CookieTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotificationScreen(
    viewModel: MawCookiesViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsState()
    val unreadCount by viewModel.unreadNotificationCount.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CookieBackground)
    ) {
        // Screen Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Notifikasi Pesanan",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = CookieTextPrimary
                )
                Text(
                    text = if (unreadCount > 0) "$unreadCount belum dibaca" else "Semua notifikasi telah dibaca",
                    fontSize = 11.sp,
                    color = CookieTextSecondary
                )
            }

            if (unreadCount > 0) {
                TextButton(onClick = { viewModel.markAllNotificationsRead() }) {
                    Text("Tandai Dibaca", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CookiePrimary)
                }
            }
        }

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔔", fontSize = 56.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Belum Ada Notifikasi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookieTextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Setiap perkembangan pesanan cookiesmu akan diberitahukan secara real-time di sini.",
                        fontSize = 12.sp,
                        color = CookieTextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notifications) { item ->
                    NotificationItemCard(
                        notification = item,
                        onClick = {
                            if (item.orderId.startsWith("MAW-")) {
                                viewModel.openTracking(item.orderId)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationItemCard(
    notification: NotificationEntity,
    onClick: () -> Unit
) {
    val (icon, iconBg, iconTint) = when (notification.type) {
        "PAYMENT", "PAYMENT_SUCCESS" -> Triple(Icons.Default.Payment, Color(0xFFE8F5E9), Color(0xFF1B5E20))
        "BAKING" -> Triple(Icons.Default.LocalFireDepartment, Color(0xFFFFF3E0), Color(0xFFE65100))
        "PACKAGING" -> Triple(Icons.Default.CheckCircle, Color(0xFFE3F2FD), Color(0xFF1565C0))
        "DELIVERING" -> Triple(Icons.Default.DeliveryDining, Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "DELIVERED" -> Triple(Icons.Default.Celebration, Color(0xFFFFF8E1), Color(0xFFF57F17))
        "PROMO" -> Triple(Icons.Default.LocalOffer, Color(0xFFFFEBEE), Color(0xFFC62828))
        else -> Triple(Icons.Default.Notifications, Color(0xFFF5F5F5), Color(0xFF616161))
    }

    val timeFormatted = remember(notification.timestamp) {
        val sdf = SimpleDateFormat("HH:mm • dd MMM", Locale("id", "ID"))
        sdf.format(Date(notification.timestamp))
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Color.White else Color(0xFFF7FBF8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .border(
                width = if (notification.isRead) 1.dp else 1.5.dp,
                color = if (notification.isRead) Color(0xFFF2ECE1) else Color(0xFFC8E6C9),
                shape = RoundedCornerShape(14.dp)
            )
            .testTag("notification_item_${notification.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = iconBg,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 13.sp,
                        fontWeight = if (notification.isRead) FontWeight.Bold else FontWeight.ExtraBold,
                        color = CookieTextPrimary
                    )
                    Text(
                        text = timeFormatted,
                        fontSize = 10.sp,
                        color = CookieTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.message,
                    fontSize = 12.sp,
                    color = CookieTextSecondary,
                    lineHeight = 16.sp
                )

                if (notification.orderId.startsWith("MAW-")) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Ketuk untuk melacak pesanan ini →",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookiePrimary
                    )
                }
            }
        }
    }
}

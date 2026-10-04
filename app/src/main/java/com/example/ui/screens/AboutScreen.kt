package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CookieBackground
import com.example.ui.theme.CookiePrimary
import com.example.ui.theme.CookieSecondary
import com.example.ui.theme.CookieTextPrimary
import com.example.ui.theme.CookieTextSecondary

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CookieBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Brand Profile
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(18.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF9F5EC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_cookie_monster),
                            contentDescription = "Maw Cookies Logo",
                            modifier = Modifier.size(68.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Maw Cookies",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2B170B)
                    )

                    Text(
                        text = "Handcrafted Gourmet Soft-Baked Cookies",
                        fontSize = 12.sp,
                        color = CookieTextSecondary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = CookiePrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("100% Halal & Bahan Alami Premium", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Maw Cookies menghadirkan cookies lembut bergaya artisan dengan racikan New Zealand Pure Butter, cokelat premium impor, dan varian unik seperti Blue Monster Oreo serta Red Velvet Cream Cheese. Setiap cookies dipanggang segar setiap hari untuk memberikan kelezatan lumer di setiap gigitan.",
                        fontSize = 12.sp,
                        color = CookieTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Kitchen Location & Hours
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFF2ECE1), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Kitchen & Toko Resmi",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CookieTextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = CookiePrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Maw Cookies Central Kitchen", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                            Text("Jl. Senopati No. 42, Kebayoran Baru, Jakarta Selatan, DKI Jakarta 12190", fontSize = 12.sp, color = CookieTextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = CookieSecondary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Jam Operasional Pemanggangan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CookieTextPrimary)
                            Text("Buka Setiap Hari: 08.00 - 22.00 WIB", fontSize = 12.sp, color = CookieTextSecondary)
                        }
                    }
                }
            }
        }

        // Storage & Freshness Guide
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFFFE082), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Tips Ketahanan & Cara Menghangatkan", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB74100))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("• Simpan di toples atau wadah kedap udara suhu ruang (tahan s/d 7 hari).", fontSize = 12.sp, color = Color(0xFF5D4037))
                    Text("• Simpan di dalam kulkas / chiller (tahan s/d 14 hari).", fontSize = 12.sp, color = Color(0xFF5D4037))
                    Text("• Panaskan di Microwave 15-20 detik atau Oven 160°C 2-3 menit sebelum disantap agar cokelat dan cream cheese lumer sempurna!", fontSize = 12.sp, color = Color(0xFF5D4037))
                }
            }
        }

        // Contact CS Button
        item {
            Button(
                onClick = {
                    Toast.makeText(context, "Membuka WhatsApp Customer Care Maw Cookies (+62 812-8765-4321)", Toast.LENGTH_LONG).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = CookiePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Hubungi WhatsApp Kami (0812-8765-4321)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

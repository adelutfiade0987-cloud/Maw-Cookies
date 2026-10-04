package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R

data class CookieProduct(
    val id: String,
    val name: String,
    val tagline: String,
    val description: String,
    val price: Int,
    val originalPrice: Int? = null,
    val weightGrams: Int,
    val rating: Float,
    val reviewsCount: Int,
    val category: String,
    @DrawableRes val drawableResId: Int,
    val badge: String? = null,
    val sweetnessLevel: Int = 4, // 1 to 5
    val ingredients: List<String> = emptyList(),
    val isFreshBakedToday: Boolean = true
)

object CookieCatalog {
    val categories = listOf(
        "Semua",
        "Signature Monster",
        "Classic Soft-Baked",
        "Stuffed Cheese",
        "Dark Chocolate",
        "Matcha Series",
        "Paket Box / Hampers"
    )

    val products = listOf(
        CookieProduct(
            id = "cookie_monster_oreo",
            name = "Blue Monster Oreo",
            tagline = "Chewy blue dough with mini Oreo & dark chips",
            description = "Signature cake-like blue monster cookie khas Maw Cookies dengan adonan chewy harum vanilla, limpahan dark choco chunks, dan topping crunchy mini Oreo di atasnya. Favorit semua kalangan!",
            price = 26000,
            originalPrice = 30000,
            weightGrams = 120,
            rating = 4.9f,
            reviewsCount = 384,
            category = "Signature Monster",
            drawableResId = R.drawable.ic_cookie_monster,
            badge = "Best Seller",
            sweetnessLevel = 4,
            ingredients = listOf("Belgian Dark Chocolate", "Mini Oreo Biscuit", "Natural Blue Spirulina", "French Butter", "Cane Sugar")
        ),
        CookieProduct(
            id = "cookie_red_velvet",
            name = "Red Velvet Cream Cheese",
            tagline = "Stuffed with melted rich cream cheese",
            description = "Adonan red velvet lembut beraroma kakao premium dengan kejutan isian New York cream cheese lumer yang gurih manis di bagian tengah. Hangatkan 15 detik untuk sensasi lumer meleleh.",
            price = 28000,
            originalPrice = 32000,
            weightGrams = 125,
            rating = 5.0f,
            reviewsCount = 412,
            category = "Stuffed Cheese",
            drawableResId = R.drawable.ic_cookie_redvelvet,
            badge = "Signature",
            sweetnessLevel = 4,
            ingredients = listOf("Philadelphia Cream Cheese", "Dutch Cocoa Powder", "Madagascar Vanilla", "Grass-fed Butter", "Buttermilk")
        ),
        CookieProduct(
            id = "cookie_choco_chip",
            name = "Classic Golden Choco Chip",
            tagline = "Golden brown crust with melted choco chunks",
            description = "Resep klasik otentik Amerika dengan pinggiran renyah dan bagian tengah chewy lembut. Menggunakan lelehan cokelat Callebaut 54% dan taburan sejumput garam laut Maldon di atasnya.",
            price = 24000,
            weightGrams = 110,
            rating = 4.8f,
            reviewsCount = 295,
            category = "Classic Soft-Baked",
            drawableResId = R.drawable.ic_cookie_chocochip,
            badge = "Favorit",
            sweetnessLevel = 3,
            ingredients = listOf("Callebaut 54% Chocolate Chunks", "Maldon Sea Salt", "Brown Sugar", "Anchor Butter", "Pure Vanilla Extract")
        ),
        CookieProduct(
            id = "cookie_dark_fudge",
            name = "Double Dark Fudge Brownie",
            tagline = "70% Dark cocoa, rich & ultra fudgy",
            description = "Khusus penggemar cokelat pekat! Kombinasi 70% dark chocolate ganache dengan adonan fudgy layaknya kue brownie hangat. Tekstur dense, tidak terlalu manis, dan sangat memanjakan lidah.",
            price = 26000,
            weightGrams = 115,
            rating = 4.9f,
            reviewsCount = 189,
            category = "Dark Chocolate",
            drawableResId = R.drawable.ic_cookie_darkfudge,
            badge = "Rich Dark",
            sweetnessLevel = 2,
            ingredients = listOf("70% Dark Cocoa Ganache", "Valrhona Cocoa Powder", "Roasted Walnuts", "Espresso Shot", "Organic Butter")
        ),
        CookieProduct(
            id = "cookie_matcha",
            name = "Uji Matcha White Choco",
            tagline = "Authentic Kyoto matcha with creamy white drops",
            description = "Dibuat langsung menggunakan bubuk matcha ceremonial grade dari Uji, Kyoto. Rasa earthy teh hijau yang khas berpadu sempurna dengan manis lembut potongan couverture white chocolate.",
            price = 27000,
            originalPrice = 30000,
            weightGrams = 115,
            rating = 4.8f,
            reviewsCount = 167,
            category = "Matcha Series",
            drawableResId = R.drawable.ic_cookie_matcha,
            badge = "Trending",
            sweetnessLevel = 3,
            ingredients = listOf("Ceremonial Uji Matcha Powder", "White Couverture Chocolate", "Almond Flakes", "New Zealand Butter")
        ),
        CookieProduct(
            id = "cookie_box_5",
            name = "Maw Party Box (Isi 5 Cookies)",
            tagline = "1 Box lengkap berisi 5 varian favorit",
            description = "Pilihan paling hemat untuk kado atau dinikmati bersama keluarga! Berisi 1 Blue Monster Oreo, 1 Red Velvet Cream Cheese, 1 Classic Choco Chip, 1 Double Dark Fudge, dan 1 Uji Matcha. Dikemas dengan box kraft mewah, kartu ucapan, dan pita satin.",
            price = 115000,
            originalPrice = 131000,
            weightGrams = 600,
            rating = 5.0f,
            reviewsCount = 520,
            category = "Paket Box / Hampers",
            drawableResId = R.drawable.ic_cookie_box,
            badge = "Hemat 15%",
            sweetnessLevel = 4,
            ingredients = listOf("5 Assorted Gourmet Cookies", "Luxury Gift Box", "Greeting Card", "Satin Ribbon")
        ),
        CookieProduct(
            id = "cookie_fresh_milk",
            name = "Fresh Farm Milk (250ml)",
            tagline = "Cold fresh milk, perfect cookie dip companion",
            description = "Susu sapi murni dingin segar dari peternakan lokal, disajikan dalam botol kaca higienis. Pasangan paling sempurna untuk mencelupkan cookies hangat Maw Cookies.",
            price = 15000,
            weightGrams = 250,
            rating = 4.9f,
            reviewsCount = 98,
            category = "Classic Soft-Baked",
            drawableResId = R.drawable.ic_cookie_milk,
            badge = "Dip Drink",
            sweetnessLevel = 1,
            ingredients = listOf("100% Pasteurized Fresh Cow Milk", "No Preservatives")
        )
    )

    fun getProductById(id: String): CookieProduct? {
        return products.find { it.id == id }
    }
}

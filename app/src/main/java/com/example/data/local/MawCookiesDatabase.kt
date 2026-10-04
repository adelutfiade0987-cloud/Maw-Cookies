package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.CartDao
import com.example.data.local.dao.NotificationDao
import com.example.data.local.dao.OrderDao
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.OrderEntity

@Database(
    entities = [
        CartItemEntity::class,
        OrderEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MawCookiesDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: MawCookiesDatabase? = null

        fun getInstance(context: Context): MawCookiesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MawCookiesDatabase::class.java,
                    "maw_cookies_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

package com.github.abusaeed_shuvo.techtrader.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.abusaeed_shuvo.techtrader.data.models.NotificationEntity
import com.github.abusaeed_shuvo.techtrader.data.models.ProductEntity


@Database(
	entities = [NotificationEntity::class, ProductEntity::class],
	version = 2,
	exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

	abstract fun notificationDao(): NotificationDao
	abstract fun productDao(): ProductDao
}
package com.github.abusaeed_shuvo.techtrader.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.abusaeed_shuvo.techtrader.data.models.NotificationEntity


@Database(
	entities = [NotificationEntity::class],
	version = 1,
	exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

	abstract fun notificationDao(): NotificationDao
}
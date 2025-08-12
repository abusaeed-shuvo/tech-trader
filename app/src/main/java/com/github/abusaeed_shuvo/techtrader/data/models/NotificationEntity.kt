package com.github.abusaeed_shuvo.techtrader.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notifications")
data class NotificationEntity(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	val title: String?,
	val body: String?,
	val timeStamp: Long = System.currentTimeMillis()
)
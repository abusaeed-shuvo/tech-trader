package com.github.abusaeed_shuvo.techtrader.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.abusaeed_shuvo.techtrader.data.models.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
	@Query("SELECT * FROM notifications")
	fun getAllNotifications(): Flow<List<NotificationEntity>>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(notificationEntity: NotificationEntity)

	@Delete
	suspend fun delete(notificationEntity: NotificationEntity)

}
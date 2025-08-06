package com.github.abusaeed_shuvo.techtrader.data.repository

import com.github.abusaeed_shuvo.techtrader.data.database.NotificationDao
import com.github.abusaeed_shuvo.techtrader.data.database.NotificationEntity
import javax.inject.Inject

class NotificationRepository @Inject constructor(
	private val dao: NotificationDao
) {
	val allNotifications = dao.getAllNotifications()

	suspend fun saveNotification(title: String?, body: String?) {
		dao.insert(NotificationEntity(title = title, body = body))
	}

	suspend fun delete(notificationEntity: NotificationEntity) = dao.delete(notificationEntity)
}
package com.github.abusaeed_shuvo.techtrader.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.abusaeed_shuvo.techtrader.data.models.NotificationEntity
import com.github.abusaeed_shuvo.techtrader.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
	private val repository: NotificationRepository
) : ViewModel() {

	val notifications = repository.allNotifications.asLiveData()

	fun removeNotification(notificationEntity: NotificationEntity) {
		viewModelScope.launch {
			repository.delete(notificationEntity)
		}
	}

	fun add(title: String, body: String) {
		viewModelScope.launch {
			repository.saveNotification(title, body)
		}
	}
}
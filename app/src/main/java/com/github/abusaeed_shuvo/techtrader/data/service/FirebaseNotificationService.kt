package com.github.abusaeed_shuvo.techtrader.data.service

import android.util.Log
import com.github.abusaeed_shuvo.techtrader.data.repository.NotificationRepository
import com.github.abusaeed_shuvo.techtrader.libs.showNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseNotificationService : FirebaseMessagingService() {

	@Inject
	lateinit var repository: NotificationRepository


	val TAG = "NOTIFICATION"

	override fun onNewToken(token: String) {
		super.onNewToken(token)
		Log.d(TAG, "onNewToken:$token ")

	}

	override fun onMessageReceived(message: RemoteMessage) {
		super.onMessageReceived(message)
		var title = message.notification?.title
		var body = message.notification?.body
		val id = System.currentTimeMillis().toInt()

		Log.d(TAG, "onMessageReceived: $title")
		Log.d(TAG, "onMessageReceived: $body")

		showNotification(this, title, body)

		CoroutineScope(Dispatchers.IO).launch {
			repository.saveNotification(title, body)
		}

	}

}
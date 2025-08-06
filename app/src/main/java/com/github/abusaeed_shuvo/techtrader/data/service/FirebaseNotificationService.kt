package com.github.abusaeed_shuvo.techtrader.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.data.repository.NotificationRepository
import com.github.abusaeed_shuvo.techtrader.ui.notifications.NotificationActivity
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
		showNotification(id, title, body)
		CoroutineScope(Dispatchers.IO).launch {
			repository.saveNotification(title, body)
		}

	}

	fun showNotification(id: Int, title: String?, body: String?) {
		var channel: NotificationChannel? = null
		var builder: NotificationCompat.Builder? = null

		var channelId = "com.github.abusaeed_shuvo.notificationapp"

		var manager: NotificationManager =
			getSystemService(NOTIFICATION_SERVICE) as NotificationManager

		var intent = Intent(this, NotificationActivity::class.java)

		var pendingIntent = PendingIntent.getActivity(
			this,
			0,
			intent,
			PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
		)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			channel =
				NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
			manager.createNotificationChannel(channel)

			builder = NotificationCompat.Builder(this, channelId)
				.setContentTitle(title)
				.setContentText(body)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_notification)
				.setContentIntent(pendingIntent)

		} else {
			builder = NotificationCompat.Builder(this, channelId)
				.setContentTitle(title)
				.setContentText(body)
				.setAutoCancel(true)
				.setSmallIcon(R.drawable.ic_notification)
				.setContentIntent(pendingIntent)
		}


		manager.notify(id, builder.build())
	}
}
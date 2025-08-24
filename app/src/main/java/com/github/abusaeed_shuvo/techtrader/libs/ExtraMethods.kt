package com.github.abusaeed_shuvo.techtrader.libs

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.doOnTextChanged
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.data.enums.FieldType
import com.github.abusaeed_shuvo.techtrader.databinding.LoadingDialogBinding
import com.github.abusaeed_shuvo.techtrader.ui.notifications.NotificationActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun setupFieldValidation(textInputLayout: TextInputLayout, fieldType: FieldType) {
	textInputLayout.editText?.doOnTextChanged { input, _, _, _ ->
		val errorMsg = validateField(input.toString(), fieldType)
		textInputLayout.error = errorMsg
	}
}


class LoadingDialog(context: Context) {
	private var dialog: AlertDialog


	init {
		val binding = LoadingDialogBinding.inflate(LayoutInflater.from(context))
		binding.loadingTitle.text = "loading..."
		dialog = MaterialAlertDialogBuilder(context)
			.setView(binding.root)
			.create()

		dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
	}

	fun show() = dialog.show()
	fun dismiss() = dialog.dismiss()
}


fun getFormattedTime(time: Long): String {
	val pattern = "dd/MM/yyyy hh:mm:ss a"

	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		val instant = Instant.ofEpochMilli(time)
		val zonedDateTime = instant.atZone(ZoneId.systemDefault())
		DateTimeFormatter.ofPattern(pattern).format(zonedDateTime)
	} else {
		val dateFormat = java.text.SimpleDateFormat(pattern, Locale.getDefault())
		val date = Date(time)
		dateFormat.format(date)
	}
}


fun showNotification(context: Context, title: String?, body: String?) {
	var channel: NotificationChannel? = null
	var builder: NotificationCompat.Builder? = null
	val id = System.currentTimeMillis().toInt()
	var channelId = "com.github.abusaeed_shuvo.notificationapp"

	var manager: NotificationManager =
		context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

	var intent = Intent(context, NotificationActivity::class.java)

	var pendingIntent = PendingIntent.getActivity(
		context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
	)

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		channel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
		manager.createNotificationChannel(channel)

		builder = NotificationCompat.Builder(context, channelId).setContentTitle(title)
			.setContentText(body).setAutoCancel(true).setSmallIcon(R.drawable.ic_notification)
			.setContentIntent(pendingIntent)

	} else {
		builder = NotificationCompat.Builder(context, channelId).setContentTitle(title)
			.setContentText(body).setAutoCancel(true).setSmallIcon(R.drawable.ic_notification)
			.setContentIntent(pendingIntent)
	}


	manager.notify(id, builder.build())
}

fun getConversationId(user1: String, user2: String): String {
	return if (user1 < user2) "${user1}_$user2" else "${user2}_$user1"
}
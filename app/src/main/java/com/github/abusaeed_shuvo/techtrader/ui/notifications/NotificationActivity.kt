package com.github.abusaeed_shuvo.techtrader.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.abusaeed_shuvo.techtrader.data.database.NotificationEntity
import com.github.abusaeed_shuvo.techtrader.databinding.ActivityNotificationBinding
import com.github.abusaeed_shuvo.techtrader.databinding.DialogNotificationBinding
import com.github.abusaeed_shuvo.techtrader.libs.showNotification
import com.github.abusaeed_shuvo.techtrader.ui.notifications.components.NotificationListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
	private lateinit var binding: ActivityNotificationBinding
	private lateinit var adapter: NotificationListAdapter

	private val viewModel: NotificationsViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		binding = ActivityNotificationBinding.inflate(layoutInflater)
		setContentView(binding.root)
		ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
			val systemBars =
				insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}

		adapter = NotificationListAdapter(onItemClick = {

			MaterialAlertDialogBuilder(this).setTitle(it.title).setMessage(it.body)
				.setNegativeButton("dismiss", null).show()
		}, onLongClick = {

		})
		binding.rcvNotifications.adapter = adapter
		binding.rcvNotifications.layoutManager = LinearLayoutManager(this)
		viewModel.notifications.observe(this) { notifications ->
			adapter.submitList(notifications)
			binding.btnClear.setOnClickListener {
				clearTasks(notifications)
			}
		}


		binding.btnAdd.setOnClickListener {
			addNoti()

		}
	}

	private fun addNoti() {
		val diaBinding = DialogNotificationBinding.inflate(
			LayoutInflater.from(this), binding.root, false
		)

		val dialog = MaterialAlertDialogBuilder(this).setView(diaBinding.root).create()

		diaBinding.apply {

			btnDisplay.setOnClickListener {
				val title = inputTitle.editText?.text.toString()
				val body = inputBody.editText?.text.toString()


				Snackbar.make(binding.root, "title:$title\nbody:$body", Snackbar.LENGTH_SHORT)
					.show()
				viewModel.add(title, body)
				showNotification(this@NotificationActivity, title, body)
				dialog.dismiss()
			}

		}


		dialog.show()
	}

	private fun clearTasks(notifications: List<NotificationEntity>) {
		val title = "Clear notifications?"

		MaterialAlertDialogBuilder(this).setTitle(title).setPositiveButton("Delete") { _, _ ->
			notifications.forEach {
				viewModel.removeNotification(it)
			}
		}.setNegativeButton("Cancel", null).show()
	}


}
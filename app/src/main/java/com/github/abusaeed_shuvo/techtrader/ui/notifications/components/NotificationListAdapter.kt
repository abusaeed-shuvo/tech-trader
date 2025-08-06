package com.github.abusaeed_shuvo.techtrader.ui.notifications.components


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.abusaeed_shuvo.techtrader.data.database.NotificationEntity
import com.github.abusaeed_shuvo.techtrader.databinding.ItemNotificationBinding
import com.github.abusaeed_shuvo.techtrader.libs.getFormattedTime

class NotificationListAdapter(
	val onItemClick: (notification: NotificationEntity) -> Unit,
	val onLongClick: (notification: NotificationEntity) -> Unit
) : ListAdapter<NotificationEntity, NotificationListAdapter.NotificationVH>(ADiffCallBack()) {
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): NotificationVH {
		return NotificationVH(
			ItemNotificationBinding
				.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false
				)
		)
	}

	override fun onBindViewHolder(
		holder: NotificationVH,
		position: Int
	) {
		val notification = getItem(position)
		holder.bind(notification)
	}

	inner class NotificationVH(private val binding: ItemNotificationBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(notificationEntity: NotificationEntity) = with(binding) {
			notificationTitle.text = notificationEntity.title
			notificationBody.text = notificationEntity.body
			notificationTime.text = getFormattedTime(notificationEntity.timeStamp)
			root.setOnClickListener {
				onItemClick(notificationEntity)
			}
		}
	}

	class ADiffCallBack : DiffUtil.ItemCallback<NotificationEntity>() {
		override fun areItemsTheSame(
			oldItem: NotificationEntity,
			newItem: NotificationEntity
		): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(
			oldItem: NotificationEntity,
			newItem: NotificationEntity
		): Boolean {
			return oldItem == newItem
		}

	}
}
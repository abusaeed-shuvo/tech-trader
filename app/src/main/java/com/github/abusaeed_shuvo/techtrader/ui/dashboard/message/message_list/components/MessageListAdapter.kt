package com.github.abusaeed_shuvo.techtrader.ui.dashboard.message.message_list.components

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.data.models.message.MessageLocalDisplay
import com.github.abusaeed_shuvo.techtrader.data.models.message.MessageType
import com.github.abusaeed_shuvo.techtrader.databinding.ItemMessageBinding
import com.github.abusaeed_shuvo.techtrader.libs.getFormattedTime

class MessageListAdapter :
	ListAdapter<MessageLocalDisplay, MessageListAdapter.MessageListVH>(DiffUt()) {
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): MessageListVH {
		return MessageListVH(
			ItemMessageBinding.inflate(
				LayoutInflater.from(parent.context), parent,
				false
			)
		)
	}

	override fun onBindViewHolder(
		holder: MessageListVH,
		position: Int
	) {
		val item = getItem(position)
		holder.bind(item)
	}


	inner class MessageListVH(private val binding: ItemMessageBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: MessageLocalDisplay) = with(binding) {
			tvMessage.text = item.message
			tvTimeStamp.text = getFormattedTime(item.timeStamp)

			when (item.messageType) {
				MessageType.SENT     -> {
					root.gravity = Gravity.END
					msgContainer.setBackgroundColor(
						ContextCompat.getColor(
							binding.root.context,
							R.color.sent
						)
					)
				}

				MessageType.RECEIVED -> {
					root.gravity = Gravity.START
					msgContainer.setBackgroundColor(
						ContextCompat.getColor(
							binding.root.context,
							R.color.received
						)
					)
				}
			}
		}
	}

	class DiffUt : DiffUtil.ItemCallback<MessageLocalDisplay>() {
		override fun areItemsTheSame(
			oldItem: MessageLocalDisplay,
			newItem: MessageLocalDisplay
		): Boolean {
			return oldItem.id == newItem.id
		}

		override fun areContentsTheSame(
			oldItem: MessageLocalDisplay,
			newItem: MessageLocalDisplay
		): Boolean {
			return oldItem == newItem
		}

	}
}
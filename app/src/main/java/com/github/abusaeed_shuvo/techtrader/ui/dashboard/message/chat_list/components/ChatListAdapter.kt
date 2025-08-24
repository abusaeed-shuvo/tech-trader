package com.github.abusaeed_shuvo.techtrader.ui.dashboard.message.chat_list.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.data.models.message.ChatListEntry
import com.github.abusaeed_shuvo.techtrader.databinding.ItemChatListBinding
import com.github.abusaeed_shuvo.techtrader.libs.getFormattedTime

class ChatListAdapter(
	val onClick: (item: ChatListEntry) -> Unit
) : ListAdapter<ChatListEntry, ChatListAdapter.ChatListVH>(DiffCallBAck()) {
	override fun onCreateViewHolder(
		parent: ViewGroup,
		viewType: Int
	): ChatListVH {
		return ChatListVH(
			ItemChatListBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		)
	}

	override fun onBindViewHolder(
		holder: ChatListVH,
		position: Int
	) {
		val item = getItem(position)
		holder.bind(item)
	}


	inner class ChatListVH(private val binding: ItemChatListBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(item: ChatListEntry) = with(binding) {
			tvPartnerName.text = item.partnerName
			tvLastMessage.text = item.lastMessage
			Glide.with(binding.root.context).load(item.partnerPic.toUri())
				.placeholder(R.drawable.ic_image_upload)
				.error(R.drawable.ic_image_upload).into(imgPartner)
			tvTimeStamp.text = getFormattedTime(item.timeStamp)
			root.setOnClickListener {
				onClick(item)
			}
		}

	}

	class DiffCallBAck : DiffUtil.ItemCallback<ChatListEntry>() {
		override fun areItemsTheSame(
			oldItem: ChatListEntry,
			newItem: ChatListEntry
		): Boolean {
			return oldItem.conversationId == newItem.conversationId
		}

		override fun areContentsTheSame(
			oldItem: ChatListEntry,
			newItem: ChatListEntry
		): Boolean {
			return oldItem == newItem
		}

	}
}
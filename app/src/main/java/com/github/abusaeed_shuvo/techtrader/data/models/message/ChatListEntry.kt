package com.github.abusaeed_shuvo.techtrader.data.models.message

data class ChatListEntry(
	val conversationId: String = "",
	val partnerId: String = "",
	val partnerName: String = "",
	val partnerPic: String = "",
	val lastMessage: String = "",
	val timeStamp: Long = System.currentTimeMillis()
)

package com.github.abusaeed_shuvo.techtrader.data.models.message

data class ChatMessage(
	val id: String = "",
	val senderId: String = "",
	val receiverId: String = "",
	val text: String = "",
	val timestamp: Long = System.currentTimeMillis()
)

fun ChatMessage.toLocal(sId: String): MessageLocalDisplay {
	return MessageLocalDisplay(
		id = id,
		message = text,
		timeStamp = timestamp,
		messageType = if (sId == senderId) MessageType.SENT else MessageType.RECEIVED
	)
}
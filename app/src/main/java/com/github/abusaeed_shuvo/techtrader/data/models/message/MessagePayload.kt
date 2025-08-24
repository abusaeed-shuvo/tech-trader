package com.github.abusaeed_shuvo.techtrader.data.models.message

data class MessagePayload(
	var conversationId: String = "",
	var senderId: String = "",
	var receiverId: String = "",
	var text: String = "",
	var partnerName: String = "",
	var partnerPic: String = ""
)

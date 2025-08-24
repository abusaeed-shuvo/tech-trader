package com.github.abusaeed_shuvo.techtrader.data.models.message

data class MessageLocalDisplay(
	var id: String = "",
	var message: String = "",
	var timeStamp: Long = 0,
	var messageType: MessageType = MessageType.SENT
)

package com.github.abusaeed_shuvo.techtrader.data.service

import com.github.abusaeed_shuvo.techtrader.data.models.message.MessagePayload
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

interface ChatService {

	fun loadMessageByUserId(uId: String)
	fun sendMessage(messagePayload: MessagePayload): Task<Void?>
	fun loadChatList(uId: String): DatabaseReference
}
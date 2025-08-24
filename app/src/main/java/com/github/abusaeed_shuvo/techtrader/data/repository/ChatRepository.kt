package com.github.abusaeed_shuvo.techtrader.data.repository

import com.github.abusaeed_shuvo.techtrader.base.Nodes
import com.github.abusaeed_shuvo.techtrader.data.models.message.ChatListEntry
import com.github.abusaeed_shuvo.techtrader.data.models.message.ChatMessage
import com.github.abusaeed_shuvo.techtrader.data.models.message.MessagePayload
import com.github.abusaeed_shuvo.techtrader.data.service.ChatService
import com.github.abusaeed_shuvo.techtrader.libs.getConversationId
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class ChatRepository @Inject constructor(
	val rdb: FirebaseDatabase
) : ChatService {
	override fun loadMessageByUserId(uId: String) {

	}

	override fun sendMessage(messagePayload: MessagePayload): Task<Void?> {
		val messageId = rdb.reference.push().key ?: return Tasks.forException(
			IllegalStateException(
				"Could not generate message ID"
			)
		)
		val message = ChatMessage(
			id = messageId,
			senderId = messagePayload.senderId,
			receiverId = messagePayload.receiverId,
			text = messagePayload.text
		)
		val chatRef = rdb.getReference(Nodes.CHATS)
			.child(messagePayload.conversationId)
			.child(Nodes.MESSAGES)
			.child(messageId)
			.setValue(message)

		val senderChatEntry = ChatListEntry(
			conversationId = messagePayload.conversationId,
			partnerId = messagePayload.receiverId,
			partnerName = messagePayload.partnerName,
			partnerPic = messagePayload.partnerPic,
			lastMessage = messagePayload.text,
			timeStamp = message.timestamp
		)
		updateChatList(messagePayload.senderId, senderChatEntry)

		val receiverChatEntry = ChatListEntry(
			conversationId = messagePayload.conversationId,
			partnerId = messagePayload.senderId,
			partnerName = messagePayload.partnerName,
			partnerPic = messagePayload.partnerPic,
			lastMessage = messagePayload.text,
			timeStamp = message.timestamp
		)
		updateChatList(messagePayload.receiverId, receiverChatEntry)


		return chatRef
	}

	fun loadMessages(cId: String): DatabaseReference {
		return rdb.getReference(Nodes.CHATS)
			.child(cId)
			.child(Nodes.MESSAGES)


	}

	override fun loadChatList(uId: String): DatabaseReference {
		return rdb.getReference(Nodes.CHAT_LIST).child(uId)
	}

	fun updateChatList(uId: String, chatListEntry: ChatListEntry): Task<Void?> {
		return rdb.getReference(Nodes.CHAT_LIST)
			.child(uId)
			.child(chatListEntry.conversationId)
			.setValue(chatListEntry)

	}

	fun createChatRoom(
		uId: String,
		rId: String,
		sName: String,
		pName: String,
		sImg: String,
		pImg: String
	): Task<Void?> {
		val cId = getConversationId(uId, rId)
		val tstp = System.currentTimeMillis()

		val receiverChatEntry = ChatListEntry(
			conversationId = cId,
			partnerId = uId,
			partnerName = sName,
			partnerPic = sImg,
			lastMessage = "",
			timeStamp = tstp
		)


		val senderChatEntry = ChatListEntry(
			conversationId = cId,
			partnerId = rId,
			partnerName = pName,
			partnerPic = pImg,
			lastMessage = "",
			timeStamp = tstp
		)

		val ref = rdb.getReference(Nodes.CHAT_LIST)
			.child(uId)
			.child(cId)
		return ref.get().continueWithTask { task ->
			if (task.isSuccessful) {
				if (task.result.exists()) {
					Tasks.forResult(null)
				} else {
					updateChatList(rId, receiverChatEntry)
						.continueWithTask {
							updateChatList(uId, senderChatEntry)
						}
				}
			} else {
				Tasks.forException(task.exception ?: Exception("Failed to check chat room"))
			}
		}

	}

	fun getChatRoomById(uId: String, cId: String): Task<DataSnapshot?> {
		return rdb.getReference(Nodes.CHAT_LIST)
			.child(uId)
			.child(cId).get()
	}

}
package com.github.abusaeed_shuvo.techtrader.ui.dashboard.message.message_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.abusaeed_shuvo.techtrader.data.models.message.ChatListEntry
import com.github.abusaeed_shuvo.techtrader.data.models.message.ChatMessage
import com.github.abusaeed_shuvo.techtrader.data.models.message.MessagePayload
import com.github.abusaeed_shuvo.techtrader.data.repository.ChatRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MessageListViewModel
@Inject constructor(
	val repo: ChatRepository
) : ViewModel() {
	private val _messageListResponse = MutableLiveData<DataState<List<ChatMessage>>>()
	val messageListResponse = _messageListResponse

	private val _chatRoomResponse = MutableLiveData<DataState<ChatListEntry>>()
	val chatRoomResponse = _chatRoomResponse

	fun loadMessages(cId: String) {
		_messageListResponse.postValue(DataState.Loading())
		repo.loadMessages(cId).addValueEventListener(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val messages =
					snapshot.children.mapNotNull { it.getValue(ChatMessage::class.java) }
				_messageListResponse.postValue(DataState.Success(messages))
			}

			override fun onCancelled(error: DatabaseError) {
				_messageListResponse.postValue(DataState.Error(error.message))

			}

		})
	}

	fun sendMessage(messagePayload: MessagePayload): Task<Void?> {
		return repo.sendMessage(messagePayload)
	}

	fun getChatRoomDetails(uId: String, cId: String) {
		_chatRoomResponse.postValue(DataState.Loading())
		repo.getChatRoomById(uId, cId).addOnSuccessListener { dataSnapshot ->
			dataSnapshot?.getValue(ChatListEntry::class.java)?.let {
				_chatRoomResponse.postValue(DataState.Success(it))
			}

		}.addOnFailureListener {
			_chatRoomResponse.postValue(DataState.Error(it.message))
		}
	}

}
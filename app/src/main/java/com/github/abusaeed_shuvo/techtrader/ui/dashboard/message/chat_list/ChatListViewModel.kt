package com.github.abusaeed_shuvo.techtrader.ui.dashboard.message.chat_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.abusaeed_shuvo.techtrader.data.models.message.ChatListEntry
import com.github.abusaeed_shuvo.techtrader.data.repository.ChatRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChatListViewModel
@Inject constructor(
	val repo: ChatRepository

) : ViewModel() {
	private val _chatListResponse = MutableLiveData<DataState<List<ChatListEntry>>>()
	val chatListResponse = _chatListResponse

	fun getChatListByUserId(uId: String) {
		_chatListResponse.postValue(DataState.Loading())
		repo.loadChatList(uId)
			.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(snapshot: DataSnapshot) {
					val list = mutableListOf<ChatListEntry>()
					for (child in snapshot.children) {
						child.getValue(ChatListEntry::class.java)?.let { list.add(it) }
					}
					_chatListResponse.postValue(DataState.Success(list.sortedByDescending { it.timeStamp }))

				}

				override fun onCancelled(error: DatabaseError) {
					_chatListResponse.postValue(DataState.Error(error.message))
				}

			})


	}

}
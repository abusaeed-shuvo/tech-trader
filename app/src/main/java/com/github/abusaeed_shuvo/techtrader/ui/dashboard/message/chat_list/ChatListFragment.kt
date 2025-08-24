package com.github.abusaeed_shuvo.techtrader.ui.dashboard.message.chat_list

import android.graphics.Color
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentChatListBinding
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.message.chat_list.components.ChatListAdapter
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatListFragment : BaseFragment<FragmentChatListBinding>(
	FragmentChatListBinding::inflate
) {
	private lateinit var swipeRefreshLayout: SwipeRefreshLayout

	private val viewModel: ChatListViewModel by viewModels()
	private lateinit var adapter: ChatListAdapter

	@Inject
	lateinit var auth: FirebaseAuth

	private var uId = ""

	override fun setListener() {
		swipeRefreshLayout = binding.swipeToRefreshLayout
		auth.currentUser?.let {
			uId = it.uid
		}
		viewModel.getChatListByUserId(uId)
		setupRecyclerView()

		swipeRefreshLayout.setOnRefreshListener {
			viewModel.getChatListByUserId(uId)
		}
		swipeRefreshLayout.setColorSchemeColors(
			Color.RED, Color.GREEN, Color.BLUE
		)
		swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.LTGRAY)

	}

	override fun setObserver() {
		viewModel.chatListResponse.observe(viewLifecycleOwner) { dataState ->
			when (dataState) {
				is DataState.Error   -> {
					binding.error.text = dataState.message
					binding.error.visibility = View.VISIBLE
					swipeRefreshLayout.isRefreshing = false
				}

				is DataState.Loading -> {
					binding.error.visibility = View.GONE
					swipeRefreshLayout.isRefreshing = true

				}

				is DataState.Success -> {
					binding.error.visibility = View.GONE
					swipeRefreshLayout.isRefreshing = false
					dataState.data?.let {
						adapter.submitList(it)
					}
				}
			}
		}

	}

	private fun setupRecyclerView() {
		adapter = ChatListAdapter(onClick = {
			val action =
				ChatListFragmentDirections.actionChatListFragmentToMessageListFragment(
					it.conversationId,

					)
			findNavController().navigate(action)

		})
		binding.rcvChatList.adapter = adapter

	}


}
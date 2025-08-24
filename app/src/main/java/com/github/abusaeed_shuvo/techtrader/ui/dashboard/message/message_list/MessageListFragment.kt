package com.github.abusaeed_shuvo.techtrader.ui.dashboard.message.message_list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.models.message.MessagePayload
import com.github.abusaeed_shuvo.techtrader.data.models.message.toLocal
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentMessageListBinding
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.message.message_list.components.MessageListAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MessageListFragment : BaseFragment<FragmentMessageListBinding>(
	FragmentMessageListBinding::inflate
) {

	private lateinit var swipeRefreshLayout: SwipeRefreshLayout
	private val args: MessageListFragmentArgs by navArgs()
	private var cId = ""
	private val viewModel: MessageListViewModel by viewModels()
	private lateinit var adapter: MessageListAdapter
	private var sId = ""
	private var rId = ""
	private var partnerName = ""
	private var partnerPic = ""


	@Inject
	lateinit var auth: FirebaseAuth

	override fun setListener() {
		swipeRefreshLayout = binding.swipeToRefreshLayout
		cId = args.conversationId
		auth.currentUser?.let {
			sId = it.uid
		}
		viewModel.loadMessages(cId)
		viewModel.getChatRoomDetails(sId, cId)
		setupRecyclerView()

		swipeRefreshLayout.setOnRefreshListener {
			viewModel.loadMessages(cId)
		}
		swipeRefreshLayout.setColorSchemeColors(
			Color.RED, Color.GREEN, Color.BLUE
		)
		swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.LTGRAY)

		binding.btnSendMessage.setOnClickListener {
			val message = binding.inputMessage.editText?.text.toString().trim()

			if (message.isEmpty()) {
				Snackbar.make(binding.root, "Message can not be empty!", Snackbar.LENGTH_SHORT)
					.show()
				return@setOnClickListener
			}
			val messagePayload = MessagePayload(
				conversationId = cId,
				senderId = sId,
				receiverId = rId,
				text = message,
				partnerName = partnerName,
				partnerPic = partnerPic
			)

			viewModel.sendMessage(
				messagePayload
			).addOnSuccessListener {
				hideKeyboard()
				binding.inputMessage.editText?.text?.clear()
			}.addOnFailureListener { exception ->
				Snackbar.make(binding.root, "${exception.message}", Snackbar.LENGTH_SHORT).show()
			}
		}

	}


	override fun setObserver() {
		viewModel.messageListResponse.observe(viewLifecycleOwner) { dataState ->
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
						val messages = it.map { msg -> msg.toLocal(sId) }
						adapter.submitList(messages)

					}
				}
			}
		}
		viewModel.chatRoomResponse.observe(viewLifecycleOwner) {
			when (it) {
				is DataState.Error   -> {

				}

				is DataState.Loading -> {

				}

				is DataState.Success -> {
					it.data?.let { item ->
						(activity as? AppCompatActivity)?.supportActionBar?.title = item.partnerName

						rId = item.partnerId
						partnerName = item.partnerName
						partnerPic = item.partnerPic

					}
				}
			}
		}

	}

	private fun setupRecyclerView() {
		val layoutManager = LinearLayoutManager(requireContext())
		layoutManager.stackFromEnd = true
		adapter = MessageListAdapter()
		binding.rcvMessageList.layoutManager = layoutManager
		binding.rcvMessageList.adapter = adapter
	}

	fun hideKeyboard() {
		val imm =
			requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		val view = requireActivity().currentFocus ?: View(requireContext())
		imm.hideSoftInputFromWindow(view.windowToken, 0)
	}

}
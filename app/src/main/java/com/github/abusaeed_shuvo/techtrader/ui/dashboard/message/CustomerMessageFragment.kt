package com.github.abusaeed_shuvo.techtrader.ui.dashboard.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.abusaeed_shuvo.techtrader.R

class CustomerMessageFragment : Fragment() {

	companion object {
		fun newInstance() = CustomerMessageFragment()
	}

	private val viewModel: CustomerMessageViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// TODO: Use the ViewModel
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		return inflater.inflate(R.layout.fragment_customer_message, container, false)
	}
}
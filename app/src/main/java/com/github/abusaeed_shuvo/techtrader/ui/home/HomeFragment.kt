package com.github.abusaeed_shuvo.techtrader.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentHomeBinding
import com.github.abusaeed_shuvo.techtrader.viewmodel.TestViewModel

class HomeFragment : Fragment() {
	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!
	private val viewModel: TestViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentHomeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)


		viewModel.currentUser.observe(viewLifecycleOwner, Observer { userEntry ->

			val userArray = userEntry.createArray()
			val arrayAdapter =
				ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, userArray)
			binding.listUser.adapter = arrayAdapter

		})

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}
package com.github.abusaeed_shuvo.techtrader.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentStartBinding


class StartFragment : Fragment() {
	private var _binding: FragmentStartBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentStartBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setListeners()
	}

	fun setListeners() = with(binding) {
		btnGetStarted.setOnClickListener {
			findNavController().navigate(R.id.action_startFragment_to_signInFragment)
		}
		btnRegister.setOnClickListener {
			findNavController().navigate(R.id.action_startFragment_to_signUpFragment)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}
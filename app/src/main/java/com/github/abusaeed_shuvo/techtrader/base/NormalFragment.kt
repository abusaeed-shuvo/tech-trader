package com.github.abusaeed_shuvo.techtrader.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class NormalFragment<VB : ViewBinding>(
	private val bindingInflater: (inflater: LayoutInflater) -> VB
) : Fragment() {
	private var _binding: VB? = null
	val binding get() = _binding!!


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = bindingInflater.invoke(inflater)
		return binding.root
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
			OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				handleBackPressed()
			}

		})
		setListener()
		setObserver()
	}

	abstract fun setListener()
	abstract fun setObserver()
	abstract fun handleBackPressed()

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
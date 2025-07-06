package com.github.abusaeed_shuvo.techtrader.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
	private var _binding: FragmentSignInBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentSignInBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setListener()
	}

	private fun setListener() = with(binding) {
		btnLogin.setOnClickListener {
			findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
		}
		btnCreateAccount.setOnClickListener {
			findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
		}

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}
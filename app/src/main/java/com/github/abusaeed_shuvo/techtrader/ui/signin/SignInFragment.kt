package com.github.abusaeed_shuvo.techtrader.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSignInBinding
import com.github.abusaeed_shuvo.techtrader.viewmodel.TestViewModel

class SignInFragment : Fragment() {
	private var _binding: FragmentSignInBinding? = null
	private val binding get() = _binding!!

	private val viewModel: TestViewModel by activityViewModels()
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
		setObservers()
	}

	private fun setListener() = with(binding) {

		btnLogin.setOnClickListener {
			val user = inputUsername.editText?.text.toString().trim()
			val password = inputPassword.editText?.text.toString()

			if (user.isBlank() || password.isEmpty()) {
				if (user.isBlank()) {
					inputUsername.error = "User Name can't be blank!"
				}
				if (password.isEmpty()) {
					inputPassword.error = "Password can't be empty!"
				}
				return@setOnClickListener
			}
			findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
		}

		btnCreateAccount.setOnClickListener {
			findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
		}

	}

	private fun setObservers() = with(binding) {
		inputUsername.editText?.doOnTextChanged { input, start, before, count ->
			inputUsername.error =
				if (input.toString().isBlank()) "User name cannot be blank!" else null
		}
		inputPassword.editText?.doOnTextChanged { input, start, before, count ->
			inputPassword.error =
				if (input.toString().isEmpty()) "Password cannot be blank!" else null
		}


	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}
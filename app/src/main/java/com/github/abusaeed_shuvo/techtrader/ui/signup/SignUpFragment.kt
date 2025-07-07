package com.github.abusaeed_shuvo.techtrader.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSignUpBinding
import com.github.abusaeed_shuvo.techtrader.viewmodel.TestViewModel


class SignUpFragment : Fragment() {
	private var _binding: FragmentSignUpBinding? = null
	private val binding get() = _binding!!

	private val viewModel: TestViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		_binding = FragmentSignUpBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setListener()
		setObservers()
	}

	private fun setListener() = with(binding) {


		btnSignup.setOnClickListener {
			val user = inputName.editText?.text.toString().trim()
			val password = inputPassword.editText?.text.toString()
			val email = inputEmail.editText?.text.toString().trim()
			val confirm = inputPasswordConfirm.editText?.text.toString()

			if (user.isBlank() || password.isEmpty() || email.isBlank() || confirm.isEmpty()) {
				if (user.isBlank()) {
					inputName.error = "Username can't be blank!"
				}
				if (password.isEmpty()) {
					inputPassword.error = "Password can't be empty!"
				}
				if (email.isBlank()) {
					inputEmail.error = "Email can't be blank!"
				}
				if (confirm.isEmpty()) {
					inputPasswordConfirm.error = "Password can't be empty!"
				}
				return@setOnClickListener
			}

			findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
		}
		btnLogin.setOnClickListener {
			findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
		}
	}

	private fun setObservers() = with(binding) {
		inputName.editText?.doOnTextChanged { input, start, end, count ->
			inputName.error = if (input.toString().isBlank()) "Username can't be blank!" else null
		}
		inputEmail.editText?.doOnTextChanged { input, start, end, count ->
			inputEmail.error = if (input.toString().isBlank()) "Email can't be blank!" else null

		}

		inputPassword.editText?.doOnTextChanged { input, start, end, count ->
			inputPassword.error = if (input.toString().isEmpty()) {
				"Password can't be empty!"
			} else if (count > 68) {
				"Password can't be longer than 64"
			} else {
				null
			}


		}
		inputPasswordConfirm.editText?.doOnTextChanged { input, start, end, count ->
			inputPasswordConfirm.error = if (input.toString().isEmpty()) {
				"Password can't be empty!"
			} else if (count > 68) {
				"Password can't be longer than 64"
			} else {
				null
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
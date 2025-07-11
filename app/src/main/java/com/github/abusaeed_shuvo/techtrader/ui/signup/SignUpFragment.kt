package com.github.abusaeed_shuvo.techtrader.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.data.enums.FieldType
import com.github.abusaeed_shuvo.techtrader.data.models.UserSignup
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSignUpBinding
import com.github.abusaeed_shuvo.techtrader.libs.setLoading
import com.github.abusaeed_shuvo.techtrader.libs.setupFieldValidation


class SignUpFragment : Fragment() {
	private var _binding: FragmentSignUpBinding? = null
	private val binding get() = _binding!!

	private val viewModel: SignUpViewModel by viewModels()

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

			if (isFormBlank()) {
				return@setOnClickListener
			}

			if (isFormIncomplete()) {
				return@setOnClickListener
			}

			val userSignup = UserSignup(
				"",
				user,
				email,
				password,
				"Seller"
			)

			viewModel.userSignup(userSignup)

			findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)

		}
		btnLogin.setOnClickListener {
			findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
		}
	}

	private fun setObservers() = with(binding) {
		val fieldMappings = mapOf(
			inputName to FieldType.USERNAME,
			inputEmail to FieldType.EMAIL,
			inputPassword to FieldType.PASSWORD,
			inputPasswordConfirm to FieldType.PASSWORD_CONFIRM
		)
		fieldMappings.forEach { (inputLayout, fieldType) ->
			setupFieldValidation(inputLayout, fieldType)
		}

		viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
			setLoading(loading, btnSignup, "Register")

		}
//		viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
//			if (navigate) {
//				findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
//			}
//		}

	}

	private fun isFormIncomplete(): Boolean {

		val email = binding.inputEmail.editText?.text.toString()
		val isEmailIncomplete = !Patterns.EMAIL_ADDRESS.matcher(email).matches()


		val password = binding.inputPassword.editText?.text.toString()
		val confirm = binding.inputPasswordConfirm.editText?.text.toString()

		val isPasswordDoesNotMatch = password != confirm
		val passwordTooSmall = password.length < 5

		if (isEmailIncomplete) {
			binding.inputEmail.error = "Invalid Email"
		}
		if (isPasswordDoesNotMatch) {
			binding.inputPasswordConfirm.error = "Password Doesn't match"
		}
		if (passwordTooSmall) {
			binding.inputPassword.error = "Password must be at least 6 character long!"
		}

		return isEmailIncomplete || isPasswordDoesNotMatch || passwordTooSmall
	}

	private fun isFormBlank(): Boolean = with(binding) {
		val user = inputName.editText?.text.toString().trim()
		val password = inputPassword.editText?.text.toString()
		val email = inputEmail.editText?.text.toString().trim()
		val confirm = inputPasswordConfirm.editText?.text.toString()


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

		return user.isBlank() || password.isEmpty() || email.isBlank() || confirm.isEmpty()
	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
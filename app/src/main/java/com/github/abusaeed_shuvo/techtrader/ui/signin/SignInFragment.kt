package com.github.abusaeed_shuvo.techtrader.ui.signin

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
import com.github.abusaeed_shuvo.techtrader.data.models.UserLogin
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSignInBinding
import com.github.abusaeed_shuvo.techtrader.libs.setLoading
import com.github.abusaeed_shuvo.techtrader.libs.setupFieldValidation

class SignInFragment : Fragment() {
	private var _binding: FragmentSignInBinding? = null
	private val binding get() = _binding!!
	private val viewModel: SignInViewModel by viewModels()

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
			val email = inputEmail.editText?.text.toString().trim()
			val password = inputPassword.editText?.text.toString()
			if (isFormBlank()) {
				return@setOnClickListener
			}
			if (isFormIncomplete()) {
				return@setOnClickListener
			}
			val user = UserLogin(
				email,
				password
			)
			viewModel.userLogin(user, binding.root)
		}
		btnCreateAccount.setOnClickListener {
			findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
		}
	}

	private fun setObservers() = with(binding) {
		val fieldMappings = mapOf(
			inputEmail to FieldType.EMAIL,
			inputPassword to FieldType.PASSWORD,
		)
		fieldMappings.forEach { (inputLayout, fieldType) ->
			setupFieldValidation(inputLayout, fieldType)
		}
		viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
			setLoading(loading, btnLogin, "Login")
		}
		viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
			if (navigate) {
				findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
			}
		}
	}

	private fun isFormBlank(): Boolean = with(binding) {
		val email = inputEmail.editText?.text.toString().trim()
		val password = inputPassword.editText?.text.toString()
		if (email.isBlank()) {
			inputEmail.error = "User Name can't be blank!"
		}
		if (password.isEmpty()) {
			inputPassword.error = "Password can't be empty!"
		}
		return email.isBlank() || password.isEmpty()
	}

	private fun isFormIncomplete(): Boolean {
		val email = binding.inputEmail.editText?.text.toString()
		val isEmailIncomplete = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
		val password = binding.inputPassword.editText?.text.toString()
		val isPassWordTooSort = password.length < 5
		if (isEmailIncomplete) {
			binding.inputEmail.error = "Invalid Email"
		}
		if (isPassWordTooSort) {
			binding.inputPassword.error = "Password must be at least 6 character long!"
		}
		return isEmailIncomplete
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
		viewModel.resetNav()
	}

}
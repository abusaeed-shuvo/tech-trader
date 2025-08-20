package com.github.abusaeed_shuvo.techtrader.ui.register.signup

import android.content.Intent
import android.util.Patterns
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.enums.FieldType
import com.github.abusaeed_shuvo.techtrader.data.models.UserSignup
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSignUpBinding
import com.github.abusaeed_shuvo.techtrader.libs.setupFieldValidation
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.CustomerDashboard
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.SellerDashboard
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate) {

	private val viewModel: SignUpViewModel by viewModels()

	@Inject
	lateinit var auth: FirebaseAuth

	@Inject
	lateinit var db: FirebaseFirestore


	override fun setListener() = with(binding) {
		btnSignup.setOnClickListener {
			val user = inputName.editText?.text.toString().trim()
			val password = inputPassword.editText?.text.toString()
			val email = inputEmail.editText?.text.toString().trim()
			val confirm = inputPasswordConfirm.editText?.text.toString()
			val userType = getStringFromToggle(binding.toggleGroupAccountType) ?: "Seller"
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
				userType
			)
			viewModel.userSignup(userSignup)
		}
		btnLogin.setOnClickListener {
			findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
		}
		val fieldMappings = mapOf(
			inputName to FieldType.USERNAME,
			inputEmail to FieldType.EMAIL,
			inputPassword to FieldType.PASSWORD,
			inputPasswordConfirm to FieldType.PASSWORD_CONFIRM
		)
		fieldMappings.forEach { (inputLayout, fieldType) ->
			setupFieldValidation(inputLayout, fieldType)
		}
	}


	override fun setObserver() = with(binding) {

		viewModel.registrationResponse.observe(viewLifecycleOwner) { dataState ->
			when (dataState) {
				is DataState.Error   -> {
					loading.dismiss()
					Snackbar.make(binding.root, "${dataState.message}", Snackbar.LENGTH_SHORT)
						.show()
				}

				is DataState.Loading -> {
					loading.show()
					Snackbar.make(binding.root, "Loading...", Snackbar.LENGTH_SHORT)
						.show()
				}

				is DataState.Success -> {
					loading.dismiss()

					loginByUserType()
				}
			}

		}
		viewModel.userTypeResponse.observe(viewLifecycleOwner) { dataState ->
			when (dataState) {
				is DataState.Error   -> {
					loading.dismiss()
					Snackbar.make(
						binding.root,
						"${dataState.message}",
						Snackbar.LENGTH_SHORT
					).show()
				}

				is DataState.Loading -> {
					loading.show()
				}

				is DataState.Success -> {
					loading.dismiss()
					val userType = dataState.data
					when (userType) {
						"Seller"   -> {
							startActivity(Intent(requireContext(), SellerDashboard::class.java))
							requireActivity().finish()
						}

						"Customer" -> {
							startActivity(
								Intent(
									requireContext(),
									CustomerDashboard::class.java
								)
							)
							requireActivity().finish()
						}

						else       -> {
							Snackbar.make(
								binding.root,
								"Failed to get userType",
								Snackbar.LENGTH_SHORT
							).show()
						}
					}
				}
			}

		}
	}

	private fun isFormIncomplete(): Boolean {
		val email = binding.inputEmail.editText?.text.toString()
		val isEmailIncomplete = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
		val password = binding.inputPassword.editText?.text.toString()
		val confirm = binding.inputPasswordConfirm.editText?.text.toString()
		val isPasswordDoesNotMatch = password != confirm
		val passwordTooSmall = password.length < 5
		val accountType = getStringFromToggle(binding.toggleGroupAccountType)
		val isAccountTypeSelected = accountType != null

		if (isAccountTypeSelected) {
			Snackbar.make(binding.root, "Please Select Account Type!", Snackbar.LENGTH_SHORT).show()
		}
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

	private fun getStringFromToggle(toggleGroup: MaterialButtonToggleGroup): String? {
		val checkedId = toggleGroup.checkedButtonId

		return if (checkedId != View.NO_ID) {
			binding.root.findViewById<MaterialButton>(checkedId).text.toString()
		} else {
			null
		}

	}

	private fun loginByUserType() {
		auth.currentUser?.let {
			viewModel.getUserTypeById(it.uid)

		}
	}
}
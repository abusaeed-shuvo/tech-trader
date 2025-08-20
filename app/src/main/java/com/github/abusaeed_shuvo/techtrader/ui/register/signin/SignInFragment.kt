package com.github.abusaeed_shuvo.techtrader.ui.register.signin

import android.content.Intent
import android.util.Patterns
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.enums.FieldType
import com.github.abusaeed_shuvo.techtrader.data.models.UserLogin
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSignInBinding
import com.github.abusaeed_shuvo.techtrader.libs.setupFieldValidation
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.CustomerDashboard
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.SellerDashboard
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {
	private val viewModel: SignInViewModel by viewModels()


	@Inject
	lateinit var auth: FirebaseAuth

	@Inject
	lateinit var db: FirebaseFirestore


	override fun setListener() = with(binding) {
		val fieldMappings = mapOf(
			inputEmail to FieldType.EMAIL,
			inputPassword to FieldType.PASSWORD,
		)
		fieldMappings.forEach { (inputLayout, fieldType) ->
			setupFieldValidation(inputLayout, fieldType)
		}
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
			viewModel.userLogin(user)
		}
		btnCreateAccount.setOnClickListener {
			findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
		}
	}

	override fun setObserver() = with(binding) {

		viewModel.loginResponse.observe(viewLifecycleOwner) { dataState ->
			when (dataState) {
				is DataState.Error   -> {
					loading.dismiss()
					Snackbar.make(binding.root, "${dataState.message}", Snackbar.LENGTH_SHORT)
						.show()
				}

				is DataState.Loading -> {
					loading.show()
					Snackbar.make(binding.root, "Loading...", Snackbar.LENGTH_SHORT).show()
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

	private fun loginByUserType() {
		auth.currentUser?.let {
			viewModel.getUserTypeById(it.uid)

		}
	}
}
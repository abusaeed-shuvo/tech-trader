package com.github.abusaeed_shuvo.techtrader.ui.register.start

import android.content.Intent
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentStartBinding
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.CustomerDashboard
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.SellerDashboard
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : BaseFragment<FragmentStartBinding>(FragmentStartBinding::inflate) {


	private val viewModel: StartViewModel by viewModels()

	@Inject
	lateinit var auth: FirebaseAuth

	@Inject
	lateinit var db: FirebaseFirestore


	override fun setListener() = with(binding) {
		loginByUserType()
		btnGetStarted.setOnClickListener {
			findNavController().navigate(R.id.action_startFragment_to_signInFragment)
		}
		btnRegister.setOnClickListener {
			findNavController().navigate(R.id.action_startFragment_to_signUpFragment)
		}

		requireActivity().onBackPressedDispatcher.addCallback(
			viewLifecycleOwner,
			object : OnBackPressedCallback(true) {
				override fun handleOnBackPressed() {
					requireActivity().finish()
				}

			})
	}


	override fun setObserver() {
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

	private fun setAutoLogin() {
		auth.currentUser?.let {

			startActivity(Intent(requireContext(), SellerDashboard::class.java))
			requireActivity().finish()
		}
	}

	private fun loginByUserType() {
		auth.currentUser?.let {
			viewModel.getUserTypeById(it.uid)

		}
	}

}
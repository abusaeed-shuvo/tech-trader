package com.github.abusaeed_shuvo.techtrader.ui.start

import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentStartBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : BaseFragment<FragmentStartBinding>(FragmentStartBinding::inflate) {

	@Inject
	lateinit var auth: FirebaseAuth

	override fun setListener() = with(binding) {
		setAutoLogin()
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

	}

	private fun setAutoLogin() {
		auth.currentUser?.let {
			findNavController().navigate(R.id.action_startFragment_to_homeFragment)
		}
	}

}
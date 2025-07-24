package com.github.abusaeed_shuvo.techtrader.ui.start

import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentStartBinding


class StartFragment : BaseFragment<FragmentStartBinding>(FragmentStartBinding::inflate) {


	override fun setListener() = with(binding) {
		btnGetStarted.setOnClickListener {
			findNavController().navigate(R.id.action_startFragment_to_signInFragment)
		}
		btnRegister.setOnClickListener {
			findNavController().navigate(R.id.action_startFragment_to_signUpFragment)
		}
	}

	override fun setObserver() {

	}


}
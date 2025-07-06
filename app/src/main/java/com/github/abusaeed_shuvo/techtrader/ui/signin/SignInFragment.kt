package com.github.abusaeed_shuvo.techtrader.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSignInBinding
import com.github.abusaeed_shuvo.techtrader.model.data.UserEntry
import com.github.abusaeed_shuvo.techtrader.viewmodel.TestViewModel
import kotlinx.coroutines.launch

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
	}

	private fun setListener() = with(binding) {
		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {


				btnLogin.setOnClickListener {
					val user = inputUsername.editText?.text.toString()
					val password = inputPassword.editText?.text.toString()
					val userEntry = UserEntry(
						user,
						password,
						"not input",
						"no input"
					)
					viewModel.setUser(userEntry)
					findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
				}
			}
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
package com.github.abusaeed_shuvo.techtrader.ui.signup

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
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSignUpBinding
import com.github.abusaeed_shuvo.techtrader.model.data.UserEntry
import com.github.abusaeed_shuvo.techtrader.viewmodel.TestViewModel
import kotlinx.coroutines.launch


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
	}

	private fun setListener() = with(binding) {
		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

				btnLogin.setOnClickListener {
					val user = inputName.editText?.text.toString()
					val password = inputPassword.editText?.text.toString()
					val email = inputEmail.editText?.text.toString()
					val confirm = inputPasswordConfirm.editText?.text.toString()
					val userEntry = UserEntry(
						user,
						password,
						confirm,
						email
					)
					viewModel.setUser(userEntry)
					findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
				}

			}
		}

		btnSignup.setOnClickListener {
			findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
package com.github.abusaeed_shuvo.techtrader.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
	private var _binding: FragmentHomeBinding? = null
	private val binding get() = _binding!!

	@Inject
	lateinit var auth: FirebaseAuth

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View? {
		_binding = FragmentHomeBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		FirebaseAuth.getInstance().currentUser?.let {
			binding.tvEmail.text = "${it.email}"
		}

		binding.btnLogout.setOnClickListener {
			MaterialAlertDialogBuilder(requireContext())
				.setTitle("Do you want to logout?")
				.setPositiveButton("Logout") { _, _ ->
					auth.signOut()
					findNavController().popBackStack(
						findNavController().graph.startDestinationId,
						false
					)
				}.setNegativeButton("Cancel", null)
				.show()
		}

		requireActivity().onBackPressedDispatcher.addCallback(
			viewLifecycleOwner,
			object : OnBackPressedCallback(true) {
				override fun handleOnBackPressed() {
					requireActivity().finish()
				}

			})

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}


}
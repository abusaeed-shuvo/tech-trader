package com.github.abusaeed_shuvo.techtrader.ui.home

import androidx.navigation.fragment.findNavController
import com.github.abusaeed_shuvo.techtrader.base.NormalFragment
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : NormalFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

	@Inject
	lateinit var auth: FirebaseAuth


	override fun setListener() {
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
	}

	override fun setObserver() {

	}

	override fun handleBackPressed() {
		requireActivity().finish()
	}

}
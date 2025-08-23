package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.profile

import android.content.Intent
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.models.UserEntity
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.DialogTextfieldInputBinding
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSellerProfileBinding
import com.github.abusaeed_shuvo.techtrader.libs.ImagePickerHelper
import com.github.abusaeed_shuvo.techtrader.ui.register.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SellerProfileFragment :
	BaseFragment<FragmentSellerProfileBinding>(FragmentSellerProfileBinding::inflate) {

	@Inject
	lateinit var auth: FirebaseAuth
	private lateinit var imagePickerHelper: ImagePickerHelper

	private val viewModel: SellerProfileViewModel by viewModels()

	override fun setListener() {
		imagePickerHelper = ImagePickerHelper(this) { uri ->
			if (uri != null) {
				viewModel.setTempImage(uri) // you already handle this in ViewModel
			}
		}
		auth.currentUser?.uid?.let {
			viewModel.getUserById(it)
		}

	}

	override fun setObserver() {
		viewModel.userDetailsState.observe(viewLifecycleOwner) { dataState ->
			binding.apply {
				when (dataState) {
					is DataState.Error   -> {
						userContainer.visibility = View.GONE
						progressIndicator.visibility = View.GONE
						tvError.visibility = View.VISIBLE
						tvError.text = dataState.message
					}

					is DataState.Loading -> {
						tvError.visibility = View.GONE
						userContainer.visibility = View.VISIBLE
						progressIndicator.visibility = View.VISIBLE
						progressIndicator.isIndeterminate = true
					}

					is DataState.Success -> {
						userContainer.visibility = View.VISIBLE
						progressIndicator.visibility = View.GONE
						tvError.visibility = View.GONE
						dataState.data?.let {
							setUi(it)
						}
					}
				}
			}


		}
		viewModel.userHasChanged.observe(viewLifecycleOwner) { changed ->
			binding.apply {
				when (changed) {
					true  -> {
						btnLogout.visibility = View.GONE
						btnCancel.visibility = View.VISIBLE
						btnSubmit.visibility = View.VISIBLE
					}

					false -> {
						btnLogout.visibility = View.VISIBLE
						btnCancel.visibility = View.GONE
						btnSubmit.visibility = View.GONE
					}
				}
			}
		}
		viewModel.tempName.observe(viewLifecycleOwner) {
			binding.tvUserNameEdit.text = "Name: ${it}"
		}
		viewModel.tempShopName.observe(viewLifecycleOwner) {
			binding.shopName.text = "Shop: ${it}"
		}
		viewModel.tempImageUri.observe(viewLifecycleOwner) {
			Glide.with(requireContext()).load(it.toString())
				.placeholder(R.drawable.ic_user).centerCrop().error(R.drawable.ic_user)
				.into(binding.imgUserProfile)
		}
	}

	private fun setUi(user: UserEntity) = with(binding) {
		tvUserNameEdit.text = "Name: ${user.name}"
		sellerId.text = "ID:${user.id}"
		sellerEmail.text = "Email: ${user.email}"
		Glide.with(requireContext()).load(viewModel.tempImageUri.value.toString())
			.placeholder(R.drawable.ic_user)
			.error(R.drawable.ic_user).into(imgUserProfile)
		btnLogout.setOnClickListener {
			MaterialAlertDialogBuilder(requireContext()).setTitle("Do you want to logout?")
				.setMessage("${auth.currentUser?.email ?: "Current user"} will be logged out!")
				.setPositiveButton("Logout") { _, _ ->
					auth.signOut()
					startActivity(Intent(requireContext(), MainActivity::class.java))
					requireActivity().finish()
				}.setNegativeButton("Dismiss", null).show()
		}
		btnSellerNameChange.setOnClickListener {
			displayNameEt {
				viewModel.setTempName(it)

			}
		}
		editShopName.setOnClickListener {
			displayNameEt {
				viewModel.setTempShopName(it)
			}
		}
		selectImage.setOnClickListener {
			imagePickerHelper.pickImage()

		}
		btnCancel.setOnClickListener {
			viewModel.resetState()
		}
		btnSubmit.setOnClickListener {
			viewModel.updateUserDetails()
		}
	}


	private fun displayNameEt(
		onClickDone: (String) -> Unit
	) {
		val dialogBinding = DialogTextfieldInputBinding.inflate(
			layoutInflater
		)

		MaterialAlertDialogBuilder(requireContext()).setTitle("Enter your name:")
			.setView(dialogBinding.root).setPositiveButton("Confirm") { dialog, which ->
				val newName = dialogBinding.etName.text.toString().trim()
				onClickDone(newName)
				dialog.dismiss()
			}.setNegativeButton("Cancel", null).show()
	}

}
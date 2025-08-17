package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.MainActivity
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.models.UserEntity
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.DialogTextfieldInputBinding
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentSellerProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SellerProfileFragment :
	BaseFragment<FragmentSellerProfileBinding>(FragmentSellerProfileBinding::inflate) {
	private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>


	@Inject
	lateinit var auth: FirebaseAuth


	private val viewModel: SellerProfileViewModel by viewModels()
	private var cameraImageUri: Uri = "".toUri()


	override fun setListener() {
		permissionRequest = getPermissionRequest()

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
		viewModel.tempImageUri.observe(viewLifecycleOwner) {
			Glide.with(requireContext()).load(it)
				.placeholder(R.drawable.ic_user).centerCrop().error(R.drawable.ic_user)
				.into(binding.imgUserProfile)
		}
	}

	private fun setUi(user: UserEntity) = with(binding) {
		tvUserNameEdit.text = "Name: ${user.name}"
		sellerId.text = "ID:${user.id}"
		sellerEmail.text = "Email: ${user.email}"
		Glide.with(requireContext()).load(viewModel.tempImageUri).placeholder(R.drawable.ic_user)
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
			displayNameEt()
		}
		selectImage.setOnClickListener {
			requestPermissions(permissionRequest, permissionList)

		}
		btnCancel.setOnClickListener {
			viewModel.resetState()
		}
		btnSubmit.setOnClickListener {
			viewModel.updateUserDetails()
		}
	}

	private fun getPermissionRequest(): ActivityResultLauncher<Array<String>> {
		return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
			if (areAllPermissionsGranted(permissionList)) {
				ImagePicker.with(this).compress(1024).maxResultSize(512, 512)
					.createIntent { intent ->
						startForUserImageResult.launch(intent)
					}
			} else {
				Snackbar.make(binding.root, "Failed to get Permissions", Snackbar.LENGTH_SHORT)
					.show()
			}
		}
	}

	companion object {
		private val permissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			arrayOf(
				Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES
			)
		} else {
			arrayOf(
				Manifest.permission.CAMERA,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE
			)
		}
	}

	private val startForUserImageResult =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
			val resultCode = activityResult.resultCode
			val data = activityResult.data

			if (resultCode == Activity.RESULT_OK) {
				val fileUri = data?.data
				if (fileUri != null) {
					cameraImageUri = fileUri
					viewModel.setTempImage(fileUri)
					Glide.with(requireContext()).load(viewModel.tempImageUri)
						.placeholder(R.drawable.ic_user).centerCrop().error(R.drawable.ic_user)
						.into(binding.imgUserProfile)
					Toast.makeText(requireContext(), "$fileUri", Toast.LENGTH_LONG).show()
				}


			} else if (resultCode == ImagePicker.RESULT_ERROR) {
				Snackbar.make(binding.root, ImagePicker.getError(data), Snackbar.LENGTH_SHORT)
					.show()
			} else {
				Snackbar.make(binding.root, "Task Cancelled!", Snackbar.LENGTH_SHORT).show()
			}

		}

	private fun displayNameEt() {
		val dialogBinding = DialogTextfieldInputBinding.inflate(
			layoutInflater
		)

		MaterialAlertDialogBuilder(requireContext()).setTitle("Enter your name:")
			.setView(dialogBinding.root).setPositiveButton("Confirm") { dialog, which ->
				val newName = dialogBinding.etName.text.toString().trim()
				viewModel.setTempName(newName)
				binding.tvUserNameEdit.text = viewModel.tempName.value
				dialog.dismiss()
			}.setNegativeButton("Cancel", null).show()
	}

}
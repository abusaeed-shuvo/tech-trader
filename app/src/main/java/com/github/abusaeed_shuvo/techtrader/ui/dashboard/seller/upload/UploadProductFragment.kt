package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.upload

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.models.ProductEntity
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentUploadProductBinding
import com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.upload.components.ImagePickerNative
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UploadProductFragment :
	BaseFragment<FragmentUploadProductBinding>(FragmentUploadProductBinding::inflate) {
	private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>
	private lateinit var picker: ImagePickerNative

	private var cameraImageUri: Uri? = null
	private val viewModel: UploadProductViewModel by viewModels()


	override fun setListener() {
		permissionRequest = getPermissionRequest()
		picker = ImagePickerNative(this) { uri ->
			cameraImageUri = uri
			Glide.with(requireContext()).load(uri).placeholder(R.drawable.ic_image_upload)
				.error(R.drawable.ic_error).into(binding.ivProduct)
		}
		with(binding) {
			ivProduct.setOnClickListener {
				val options = arrayOf("Native", "Dhaval2404")
				MaterialAlertDialogBuilder(requireContext()).setTitle("Select Image Picker:")
					.setItems(options) { _, which ->
						when (which) {
							0 -> picker.launch()
							1 -> requestPermissions(permissionRequest, permissionList)
						}
					}.show()
			}
			btnAddProduct.setOnClickListener {
				val name = inputProductName.editText?.text.toString()
				val desc = inputProductDescription.editText?.text.toString()
				val price = inputProductPrice.editText?.text.toString().toDouble()
				val quantity = inputProductQuantity.editText?.text.toString().toInt()
				val imageLink = cameraImageUri.toString()
				val sellerId = ""

				val product = ProductEntity(name, desc, price, quantity, imageLink, sellerId)
				viewModel.add(product)
			}
		}
	}

	override fun setObserver() {
	}

	private fun getPermissionRequest(): ActivityResultLauncher<Array<String>> {
		return registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
			if (areAllPermissionsGranted(permissionList)) {
				ImagePicker.with(this).compress(1024).maxResultSize(720, 1080)
					.createIntent { intent ->
						startForProductImageResult.launch(intent)
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

	private val startForProductImageResult =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
			val resultCode = activityResult.resultCode
			val data = activityResult.data

			if (resultCode == Activity.RESULT_OK) {
				val fileUri = data?.data!!
				cameraImageUri = fileUri
				Glide.with(requireContext()).load(fileUri).placeholder(R.drawable.ic_image_upload)
					.error(R.drawable.ic_image_upload).into(binding.ivProduct)
			} else if (resultCode == ImagePicker.RESULT_ERROR) {
				Snackbar.make(binding.root, ImagePicker.getError(data), Snackbar.LENGTH_SHORT)
					.show()
			} else {
				Snackbar.make(binding.root, "Task Cancelled!", Snackbar.LENGTH_SHORT).show()
			}

		}

}
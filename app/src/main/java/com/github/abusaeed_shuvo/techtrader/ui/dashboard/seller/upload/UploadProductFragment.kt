package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.upload

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentUploadProductBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject


@AndroidEntryPoint
class UploadProductFragment :
	BaseFragment<FragmentUploadProductBinding>(FragmentUploadProductBinding::inflate) {
	private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>

	@Inject
	lateinit var auth: FirebaseAuth

	private var cameraImageUri: Uri = "".toUri()
	private val viewModel: UploadProductViewModel by viewModels()


	override fun setListener() {
		permissionRequest = getPermissionRequest()

		with(binding) {
			ivProduct.setOnClickListener {
				requestPermissions(permissionRequest, permissionList)
			}

			btnAddProduct.setOnClickListener {
				val name = inputProductName.editText?.text.toString()
				val desc = inputProductDescription.editText?.text.toString()
				val price = inputProductPrice.editText?.text.toString().toDouble()
				val quantity = inputProductQuantity.editText?.text.toString().toInt()
				val sellerId = auth.currentUser?.uid ?: ""


				val product = Product(
					name,
					desc,
					price,
					quantity,
					cameraImageUri.toString(),
					sellerId,
					UUID.randomUUID().toString()
				)
				uploadProduct(product)

				findNavController().popBackStack()
			}
		}
	}

	override fun setObserver() {

		viewModel.productUploadResponse.observe(viewLifecycleOwner) {
			when (it) {
				is DataState.Error   -> {

					Snackbar.make(
						binding.root, "${it.message}", Snackbar.LENGTH_SHORT
					).show()
				}

				is DataState.Loading -> {

					Snackbar.make(
						binding.root, "Uploading Product!", Snackbar.LENGTH_LONG
					).show()
				}

				is DataState.Success -> {
					Snackbar.make(
						binding.root, "Product added successfully!", Snackbar.LENGTH_SHORT
					).show()

				}
			}
		}
	}

	private fun uploadProduct(product: Product) {
		viewModel.productUpload(product)
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
				val fileUri = data?.data
				if (fileUri != null) {
					cameraImageUri = fileUri
					Glide.with(requireContext()).load(fileUri)
						.placeholder(R.drawable.ic_image_upload).error(R.drawable.ic_error)
						.into(binding.ivProduct)
					Toast.makeText(requireContext(), "$fileUri", Toast.LENGTH_LONG).show()
				}


			} else if (resultCode == ImagePicker.RESULT_ERROR) {
				Snackbar.make(binding.root, ImagePicker.getError(data), Snackbar.LENGTH_SHORT)
					.show()
			} else {
				Snackbar.make(binding.root, "Task Cancelled!", Snackbar.LENGTH_SHORT).show()
			}

		}

}
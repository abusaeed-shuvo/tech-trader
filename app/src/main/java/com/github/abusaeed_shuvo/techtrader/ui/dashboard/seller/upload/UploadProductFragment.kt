package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.upload

import android.net.Uri
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.abusaeed_shuvo.techtrader.R
import com.github.abusaeed_shuvo.techtrader.base.BaseFragment
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import com.github.abusaeed_shuvo.techtrader.databinding.FragmentUploadProductBinding
import com.github.abusaeed_shuvo.techtrader.libs.ImagePickerHelper
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject


@AndroidEntryPoint
class UploadProductFragment :
	BaseFragment<FragmentUploadProductBinding>(FragmentUploadProductBinding::inflate) {
	private lateinit var imagePickerHelper: ImagePickerHelper

	@Inject
	lateinit var auth: FirebaseAuth

	private var cameraImageUri: Uri = "".toUri()
	private val viewModel: UploadProductViewModel by viewModels()


	override fun setListener() {
		imagePickerHelper = ImagePickerHelper(this) { uri ->
			if (uri != null) {
				cameraImageUri = uri
				Glide.with(requireContext()).load(uri).placeholder(R.drawable.ic_image_upload)
					.error(R.drawable.ic_error).into(binding.ivProduct)
			}
		}

		with(binding) {
			ivProduct.setOnClickListener {
				imagePickerHelper.pickImage()
			}

			btnAddProduct.setOnClickListener {
				val name = inputProductName.editText?.text.toString().trim()
				val desc = inputProductDescription.editText?.text.toString().trim()
				val price = inputProductPrice.editText?.text.toString().toDoubleOrNull() ?: 0.0
				val quantity = inputProductQuantity.editText?.text.toString().toIntOrNull() ?: 0
				val sellerId = auth.currentUser?.uid ?: ""

				if (name.isEmpty() || desc.isEmpty() || price < 0.0 || quantity < 0 || cameraImageUri.toString()
						.isEmpty()
				) {
					Snackbar.make(
						binding.root, "Blank Entry not allowed!", Snackbar.LENGTH_SHORT
					).show()
					return@setOnClickListener
				}

				val product = Product(
					name = name,
					description = desc,
					price = price,
					quantity = quantity.toLong(),
					imageLink = cameraImageUri.toString(),
					sellerId = sellerId,
					productId = UUID.randomUUID().toString()
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


}
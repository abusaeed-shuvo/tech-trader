package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.upload

import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.data.repository.SellerRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UploadProductViewModel @Inject constructor(
	private val repository: SellerRepository
) : ViewModel() {
	private val _productUploadResponse = MutableLiveData<DataState<String>>()

	val productUploadResponse = _productUploadResponse


	fun productUpload(product: Product) {
		_productUploadResponse.postValue(DataState.Loading())

		repository.uploadProductImage(product.imageLink.toUri())
			.addOnSuccessListener { taskSnapshot ->
				taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
					product.imageLink = uri.toString()

					repository.uploadProduct(product).addOnSuccessListener {
						_productUploadResponse.postValue(DataState.Success("Uploaded Product successfully!"))
					}.addOnFailureListener { exception ->
						_productUploadResponse.postValue(DataState.Error("${exception.message}"))

					}
				}

			}.addOnFailureListener { exception ->
				_productUploadResponse.postValue(DataState.Error("${exception.message}"))

			}
	}

}
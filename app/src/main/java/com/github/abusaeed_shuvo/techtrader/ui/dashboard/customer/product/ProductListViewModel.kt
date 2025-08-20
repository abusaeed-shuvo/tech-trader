package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.abusaeed_shuvo.techtrader.data.models.Product
import com.github.abusaeed_shuvo.techtrader.data.repository.CustomerRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel
@Inject constructor(
	private val repository: CustomerRepository
) : ViewModel() {

	private val _productResponse = MutableLiveData<DataState<List<Product>>>()

	val productResponse = _productResponse

	fun getProductById(userId: String) {
		_productResponse.postValue(DataState.Loading())

		repository.getAllProducts().addOnSuccessListener { documentSnapshots ->
			val productList = mutableListOf<Product>()

			documentSnapshots.documents.forEach { documentSnapshot ->
				documentSnapshot.toObject(Product::class.java)?.let {
					productList.add(it)
				}
			}
			_productResponse.postValue(DataState.Success(productList))
		}.addOnFailureListener { exception ->
			_productResponse.postValue(DataState.Error("${exception.message}"))

		}
	}

}
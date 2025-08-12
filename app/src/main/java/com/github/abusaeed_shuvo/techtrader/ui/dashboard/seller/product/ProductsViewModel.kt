package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.abusaeed_shuvo.techtrader.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProductsViewModel
@Inject constructor(
	private val repository: ProductRepository
) : ViewModel() {

	val products = repository.allProducts.asLiveData()

}
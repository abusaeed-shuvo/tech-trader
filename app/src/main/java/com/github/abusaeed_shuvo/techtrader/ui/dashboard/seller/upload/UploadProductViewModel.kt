package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.abusaeed_shuvo.techtrader.data.models.ProductEntity
import com.github.abusaeed_shuvo.techtrader.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UploadProductViewModel @Inject constructor(
	private val repository: ProductRepository
) : ViewModel() {


	fun add(productEntity: ProductEntity) {
		viewModelScope.launch {
			repository.saveNotification(productEntity)
		}
	}
}
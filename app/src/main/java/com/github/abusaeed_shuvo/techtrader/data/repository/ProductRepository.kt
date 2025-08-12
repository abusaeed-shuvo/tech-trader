package com.github.abusaeed_shuvo.techtrader.data.repository

import com.github.abusaeed_shuvo.techtrader.data.database.ProductDao
import com.github.abusaeed_shuvo.techtrader.data.models.ProductEntity
import javax.inject.Inject

class ProductRepository @Inject constructor(
	private val dao: ProductDao
) {
	val allProducts = dao.getAllProducts()

	suspend fun saveNotification(productEntity: ProductEntity) {
		dao.insert(productEntity)
	}

	suspend fun delete(productEntity: ProductEntity) = dao.delete(productEntity)
}
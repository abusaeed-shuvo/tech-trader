package com.github.abusaeed_shuvo.techtrader.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
	var name: String = "",
	var description: String = "",
	var price: Double = 0.0,
	var quantity: Int = 0,
	var imageLink: String = "",
	var sellerId: String = "",
	@PrimaryKey(autoGenerate = true) var productId: Int = 0
)

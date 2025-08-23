package com.github.abusaeed_shuvo.techtrader.data.models

data class Product(
	var name: String = "",
	var description: String = "",
	var price: Double = 0.0,
	var quantity: Long = 0,
	var avgRating: Double = 0.0,
	var sold: Long = 0,
	var visitCount: Long = 0,
	var ratingCount: Long = 0,
	var discount: Int = 0,
	var imageLink: String = "",
	var sellerId: String = "",
	val productId: String = "",
)

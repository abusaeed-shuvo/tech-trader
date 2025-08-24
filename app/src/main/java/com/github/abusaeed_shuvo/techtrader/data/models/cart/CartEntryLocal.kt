package com.github.abusaeed_shuvo.techtrader.data.models.cart

data class CartEntryLocal(
	var productId: String = "",
	var productName: String = "",
	var productImage: String,
	var price: Double = 0.0,
	var quantity: Long = 0,
	var totalPrice: Double = 0.0,
	var availableQuantity: Long = 0
)

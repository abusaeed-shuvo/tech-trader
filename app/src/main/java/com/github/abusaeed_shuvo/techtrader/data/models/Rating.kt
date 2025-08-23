package com.github.abusaeed_shuvo.techtrader.data.models

data class Rating(
	val userId: String = "",
	val name: String = "",
	val rating: Int = 0,
	val review: String = "",
	val timeStamp: Long = System.currentTimeMillis()
)

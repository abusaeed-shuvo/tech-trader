package com.github.abusaeed_shuvo.techtrader.data.models

data class UserSignup(
	var id: String,
	val name: String,
	val email: String,
	val password: String,
	val userType: String
)

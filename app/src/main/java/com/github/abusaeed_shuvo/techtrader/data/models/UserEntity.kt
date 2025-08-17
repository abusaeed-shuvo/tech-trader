package com.github.abusaeed_shuvo.techtrader.data.models

data class UserEntity(
	var id: String = "",
	var name: String = "",
	val email: String = "",
	val password: String = "",
	var userType: String = "",
	var profileImageLink: String = ""
)

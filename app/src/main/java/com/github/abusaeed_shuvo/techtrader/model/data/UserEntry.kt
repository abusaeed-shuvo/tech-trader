package com.github.abusaeed_shuvo.techtrader.model.data

data class UserEntry(
	val userName: String,
	val password: String,
	val confirmPassword: String,
	val email: String
) {

	fun createArray(): Array<String> {
		val currentList = listOf(userName, password, confirmPassword, email)
		return currentList.toTypedArray()
	}

}

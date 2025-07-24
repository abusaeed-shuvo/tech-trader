package com.github.abusaeed_shuvo.techtrader.data.state

sealed class DataState<T>(
	var data: T? = null,
	var message: String? = null
) {
	class Loading<T> : DataState<T>()

	class Success<T>(mData: T?) : DataState<T>(data = mData)

	class Error<T>(message: String?) : DataState<T>(message = message)
}
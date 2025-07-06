package com.github.abusaeed_shuvo.techtrader.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.abusaeed_shuvo.techtrader.model.data.UserEntry

class TestViewModel : ViewModel() {
	private val _currentUser = MutableLiveData<UserEntry>()
	val currentUser = _currentUser


	fun setUser(userEntry: UserEntry) {
		_currentUser.value = userEntry
	}
}
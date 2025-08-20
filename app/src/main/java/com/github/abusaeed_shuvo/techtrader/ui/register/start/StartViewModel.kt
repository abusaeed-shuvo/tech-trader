package com.github.abusaeed_shuvo.techtrader.ui.register.start

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.abusaeed_shuvo.techtrader.base.Nodes
import com.github.abusaeed_shuvo.techtrader.data.repository.AuthRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
	private val authRepository: AuthRepository
) : ViewModel() {
	private val _userTypeResponse = MutableLiveData<DataState<String>>()
	val userTypeResponse get() = _userTypeResponse

	fun getUserTypeById(uid: String) {
		_userTypeResponse.postValue(DataState.Loading())
		authRepository.getUserTypeById(uid)
			.addOnSuccessListener { documentSnapshot ->

				val userType = documentSnapshot?.getString(Nodes.USER_TYPE)
				if (userType != null) {
					_userTypeResponse.postValue(DataState.Success(userType))

				} else {
					_userTypeResponse.postValue(DataState.Error("Login to get started"))
				}
			}.addOnFailureListener {
				_userTypeResponse.postValue(DataState.Error(it.message))
			}
	}
}
package com.github.abusaeed_shuvo.techtrader.ui.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.abusaeed_shuvo.techtrader.data.models.UserLogin
import com.github.abusaeed_shuvo.techtrader.data.repository.AuthRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
	val TAG = "LOGIN"

	private val _loginResponse = MutableLiveData<DataState<UserLogin>>()
	val loginResponse get() = _loginResponse

	fun userLogin(userLogin: UserLogin) {
		val authService = AuthRepository()
		_loginResponse.postValue(DataState.Loading())
		viewModelScope.launch {
			authService.userLogin(userLogin).addOnSuccessListener {
				_loginResponse.postValue(DataState.Success(userLogin))
				Log.d(TAG, "userLogin: Success")
			}.addOnFailureListener { exception ->
				_loginResponse.postValue(DataState.Error(exception.message))
				Log.d(TAG, "userLogin: ${exception.message}")
			}
		}
	}


}
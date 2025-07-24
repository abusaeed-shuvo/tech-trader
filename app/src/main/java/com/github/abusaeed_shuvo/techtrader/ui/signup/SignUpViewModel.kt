package com.github.abusaeed_shuvo.techtrader.ui.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.abusaeed_shuvo.techtrader.data.models.UserSignup
import com.github.abusaeed_shuvo.techtrader.data.repository.AuthRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

	private val _registrationResponse = MutableLiveData<DataState<UserSignup>>()
	val registrationResponse get() = _registrationResponse

	val TAG = "SIGNUP"

	fun userSignup(userSignup: UserSignup) {
		val authService = AuthRepository()
		_registrationResponse.postValue(DataState.Loading())
		viewModelScope.launch {

			authService.userRegistration(userSignup).addOnSuccessListener {
				_registrationResponse.postValue(DataState.Success(userSignup))
				Log.d(TAG, "Register: Success - ${it.user?.email}")

			}.addOnFailureListener { exception ->
				_registrationResponse.postValue(DataState.Error(exception.message))
				Log.d(TAG, "Register: Failed-${exception.message}")
			}

		}
	}


}
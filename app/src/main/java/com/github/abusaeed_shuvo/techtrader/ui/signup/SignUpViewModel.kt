package com.github.abusaeed_shuvo.techtrader.ui.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.abusaeed_shuvo.techtrader.data.models.UserSignup
import com.github.abusaeed_shuvo.techtrader.data.repository.AuthRepository
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
	private val _isLoading = MutableLiveData(false)
	private val _navigate = MutableLiveData(false)
	val isLoading get() = _isLoading
	val navigate get() = _navigate

	val TAG = "SIGNUP"

	fun userSignup(userSignup: UserSignup) {
		val authService = AuthRepository()
		_isLoading.value = true

		viewModelScope.launch {

//			try {
//				val result: AuthResult = authService.userRegistration(userSignup).await()
//				Log.d(TAG, "userSignup: Success - ${result.user?.uid}")
//				_navigate.value = true
//			} catch (e: Exception) {
//				Log.d(TAG, "userSignup: Failed-${e.message}")
//				_navigate.value = false
//			} finally {
//				_isLoading.value = false
//			}
			authService.userRegistration(userSignup)
				.addOnSuccessListener {
					Log.d(TAG, "userSignup: Success")
					_isLoading.value = false
					_navigate.value = true
				}
				.addOnFailureListener {
					Log.d(TAG, "userSignup: ${it.message}")
					_isLoading.value = false
					_navigate.value = false
				}
		}


	}

}
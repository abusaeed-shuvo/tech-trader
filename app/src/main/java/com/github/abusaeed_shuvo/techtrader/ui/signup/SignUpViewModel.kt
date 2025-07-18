package com.github.abusaeed_shuvo.techtrader.ui.signup

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.abusaeed_shuvo.techtrader.data.models.UserSignup
import com.github.abusaeed_shuvo.techtrader.data.repository.AuthRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignUpViewModel : ViewModel() {
	private val _isLoading = MutableLiveData(false)
	private val _navigate = MutableLiveData(false)
	val isLoading get() = _isLoading
	val navigate get() = _navigate

	val TAG = "SIGNUP"

	fun userSignup(userSignup: UserSignup, view: View) {
		val authService = AuthRepository()
		_isLoading.value = true
		viewModelScope.launch {

			try {
				val result: AuthResult = authService.userRegistration(userSignup).await()
				Log.d(TAG, "Register: Success - ${result.user?.email}")
				val msg = "$TAG ${result.user?.email} successfully!"
				Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

				_navigate.value = true
			} catch (e: Exception) {
				Log.d(TAG, "Register: Failed-${e.message}")
				val msg = "$TAG failed-${e.message}"
				Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

				_navigate.value = false
			} finally {
				_isLoading.value = false
			}

		}
	}

	fun resetNav() {
		_navigate.value = false
	}

}
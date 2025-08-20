package com.github.abusaeed_shuvo.techtrader.ui.dashboard.customer.profile

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.abusaeed_shuvo.techtrader.data.models.UserEntity
import com.github.abusaeed_shuvo.techtrader.data.repository.CustomerRepository
import com.github.abusaeed_shuvo.techtrader.data.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomerProfileViewModel @Inject constructor(
	private val customerService: CustomerRepository

) : ViewModel() {
	private val _userDetailsState = MutableLiveData<DataState<UserEntity>>()
	val userDetailsState = _userDetailsState

	private val _userHasChanged = MutableLiveData(false)
	val userHasChanged = _userHasChanged

	private val _tempName = MutableLiveData<String>()
	val tempName = _tempName

	private val _tempImageUri = MutableLiveData<Uri>()
	val tempImageUri = _tempImageUri

	private var originalUser: UserEntity? = null

	fun setTempName(name: String) {
		_tempName.value = name
		checkIfUserChanged()
	}

	fun setTempImage(uri: Uri) {
		_tempImageUri.value = uri
		checkIfUserChanged()
	}

	fun getUserById(userId: String) {
		_userDetailsState.postValue(DataState.Loading())

		customerService.getUserDataById(userId).addOnSuccessListener { documentSnapshot ->
			val user = documentSnapshot?.toObject(UserEntity::class.java)

			if (user != null) {
				originalUser = user.copy()
				_tempName.value = user.name
				_tempImageUri.value = user.profileImageLink.toUri()
				_userDetailsState.postValue(DataState.Success(user))
				checkIfUserChanged()
			} else {
				_userDetailsState.postValue(DataState.Error("User not found"))
			}
		}.addOnFailureListener { exception ->
			_userDetailsState.postValue(DataState.Error("${exception.message}"))
		}
	}

	fun updateUserDetails() {
		val user = originalUser ?: return
		val newName = _tempName.value ?: user.name
		val newImageUri = _tempImageUri.value

		_userDetailsState.postValue(DataState.Loading())

		if (newImageUri?.toString() != user.profileImageLink) {
			customerService.uploadUserImage(newImageUri!!, user.id)
				.addOnSuccessListener { taskSnapshot ->
					taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->

						val updatedUser = user.copy(
							name = newName,
							profileImageLink = uri.toString()
						)
						updateUserInDb(updatedUser)
					}
				}.addOnFailureListener { exception ->
					_userDetailsState.postValue(DataState.Error("${exception.message}"))
				}

		} else {
			val updatedUser = user.copy(name = newName)
			updateUserInDb(updatedUser)
		}


	}

	private fun updateUserInDb(userEntity: UserEntity) {
		customerService.updateUserProfile(userEntity)
			.addOnSuccessListener {
				getUserById(userEntity.id)
				_userHasChanged.postValue(false)
			}
			.addOnFailureListener { exception ->
				_userDetailsState.postValue(DataState.Error("${exception.message}"))
			}
	}

	private fun checkIfUserChanged() {
		val user = originalUser ?: return

		val hasChanged =
			(_tempName.value != user.name) || (_tempImageUri.value?.toString() != user.profileImageLink)

		_userHasChanged.postValue(hasChanged)
	}

	fun resetState() {
		_tempName.value = originalUser?.name
		_tempImageUri.value = originalUser?.profileImageLink?.toUri()
		_userHasChanged.postValue(false)
	}
}
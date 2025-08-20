package com.github.abusaeed_shuvo.techtrader.libs

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar

class ImagePickerHelper(
	private val fragment: Fragment,
	private val onImagePicked: (Uri?) -> Unit
) {
	private lateinit var permissionRequest: ActivityResultLauncher<Array<String>>
	private val startForUserImageResult: ActivityResultLauncher<Intent>

	init {
		// Image picker result
		startForUserImageResult =
			fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
				val resultCode = activityResult.resultCode
				val data = activityResult.data

				when (resultCode) {
					Activity.RESULT_OK       -> {
						val fileUri = data?.data
						onImagePicked(fileUri)
					}

					ImagePicker.RESULT_ERROR -> {
						Snackbar.make(
							fragment.requireView(),
							ImagePicker.getError(data),
							Snackbar.LENGTH_SHORT
						).show()
						onImagePicked(null)
					}

					else                     -> {
						Snackbar.make(
							fragment.requireView(),
							"Task Cancelled!",
							Snackbar.LENGTH_SHORT
						).show()
						onImagePicked(null)
					}
				}
			}

		// Permission request
		permissionRequest =
			fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
				if (areAllPermissionsGranted(permissionList)) {
					ImagePicker.with(fragment)
						.compress(1024)
						.maxResultSize(512, 512)
						.createIntent { intent ->
							startForUserImageResult.launch(intent)
						}
				} else {
					Snackbar.make(
						fragment.requireView(),
						"Failed to get Permissions",
						Snackbar.LENGTH_SHORT
					).show()
				}
			}
	}

	fun pickImage() {
		permissionRequest.launch(permissionList)
	}

	private fun areAllPermissionsGranted(permissions: Array<String>): Boolean {
		return permissions.all {
			ContextCompat.checkSelfPermission(
				fragment.requireContext(),
				it
			) == PackageManager.PERMISSION_GRANTED
		}
	}

	companion object {
		val permissionList: Array<String> =
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				arrayOf(
					Manifest.permission.CAMERA,
					Manifest.permission.READ_MEDIA_IMAGES
				)
			} else {
				arrayOf(
					Manifest.permission.CAMERA,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE
				)
			}
	}

}
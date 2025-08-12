package com.github.abusaeed_shuvo.techtrader.ui.dashboard.seller.upload.components

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class ImagePickerNative(
	private val fragment: Fragment,
	private val onImagePicked: (Uri) -> Unit
) {
	private val context get() = fragment.requireContext()
	private val pickImageLauncher = fragment.registerForActivityResult(
		ActivityResultContracts.GetContent()
	) { uri -> uri?.let(onImagePicked) }
	private val takePictureLauncher = fragment.registerForActivityResult(
		ActivityResultContracts.TakePicture()
	) { success -> if (success) lastCameraUri?.let(onImagePicked) }
	private val requestPermissionsLauncher = fragment.registerForActivityResult(
		ActivityResultContracts.RequestMultiplePermissions()
	) { result ->
		if (result.all { it.value }) {
			showImageSourceDialog()
		} else {
			Toast.makeText(
				context,
				"Camera and Storage permissions are required",
				Toast.LENGTH_SHORT
			).show()
		}
	}
	private var lastCameraUri: Uri? = null
	fun launch() {
		val permissions = mutableListOf(Manifest.permission.CAMERA).apply {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
				add(Manifest.permission.READ_MEDIA_IMAGES)
			else
				add(Manifest.permission.READ_EXTERNAL_STORAGE)
		}
		requestPermissionsLauncher.launch(permissions.toTypedArray())
	}

	private fun showImageSourceDialog() {
		MaterialAlertDialogBuilder(context)
			.setTitle("Select Image Source")
			.setItems(arrayOf("Camera", "Gallery")) { _, which ->
				if (which == 0) openCamera() else pickImageLauncher.launch("image/*")
			}
			.show()
	}

	private fun openCamera() {
		val imageFile = File.createTempFile("IMG_", ".jpg", context.cacheDir)
		lastCameraUri =
			FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
		takePictureLauncher.launch(lastCameraUri)
	}
}
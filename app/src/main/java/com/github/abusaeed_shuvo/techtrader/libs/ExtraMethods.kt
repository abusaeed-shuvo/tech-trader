package com.github.abusaeed_shuvo.techtrader.libs

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.doOnTextChanged
import com.github.abusaeed_shuvo.techtrader.data.enums.FieldType
import com.github.abusaeed_shuvo.techtrader.databinding.LoadingDialogBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

fun setupFieldValidation(textInputLayout: TextInputLayout, fieldType: FieldType) {
	textInputLayout.editText?.doOnTextChanged { input, _, _, _ ->
		val errorMsg = validateField(input.toString(), fieldType)
		textInputLayout.error = errorMsg
	}
}

fun setLoading(isLoading: Boolean, button: MaterialButton, btnText: String) {
	if (isLoading) {
		button.isEnabled = false
		button.text = "Loading..."


	} else {
		button.isEnabled = true
		button.text = btnText
	}
}

class LoadingDialog(context: Context) {
	private var dialog: AlertDialog


	init {
		val binding = LoadingDialogBinding.inflate(LayoutInflater.from(context))
		binding.loadingTitle.text = "loading..."
		dialog = MaterialAlertDialogBuilder(context)
			.setView(binding.root)
			.create()

		dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
	}

	fun show() = dialog.show()
	fun dismiss() = dialog.dismiss()
}
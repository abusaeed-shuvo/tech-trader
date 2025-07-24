package com.github.abusaeed_shuvo.techtrader.libs

import android.app.ProgressDialog
import android.content.Context
import androidx.core.widget.doOnTextChanged
import com.github.abusaeed_shuvo.techtrader.data.enums.FieldType
import com.google.android.material.button.MaterialButton
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
	private val dialog: ProgressDialog = ProgressDialog(context)

	init {
		dialog.setTitle("Loading...")
		dialog.setMessage("Please wait.")
	}

	fun show() = dialog.show()
	fun dismiss() = dialog.dismiss()
}
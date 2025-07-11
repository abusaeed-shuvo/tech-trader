package com.github.abusaeed_shuvo.techtrader.libs

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
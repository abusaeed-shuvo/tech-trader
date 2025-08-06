package com.github.abusaeed_shuvo.techtrader.libs

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.doOnTextChanged
import com.github.abusaeed_shuvo.techtrader.data.enums.FieldType
import com.github.abusaeed_shuvo.techtrader.databinding.LoadingDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun setupFieldValidation(textInputLayout: TextInputLayout, fieldType: FieldType) {
	textInputLayout.editText?.doOnTextChanged { input, _, _, _ ->
		val errorMsg = validateField(input.toString(), fieldType)
		textInputLayout.error = errorMsg
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


fun getFormattedTime(time: Long): String {
	val pattern = "dd/MM/yyyy hh:mm:ss a"

	return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
		val instant = Instant.ofEpochMilli(time)
		val zonedDateTime = instant.atZone(ZoneId.systemDefault())
		DateTimeFormatter.ofPattern(pattern).format(zonedDateTime)
	} else {
		val dateFormat = java.text.SimpleDateFormat(pattern, Locale.getDefault())
		val date = Date(time)
		dateFormat.format(date)
	}
}

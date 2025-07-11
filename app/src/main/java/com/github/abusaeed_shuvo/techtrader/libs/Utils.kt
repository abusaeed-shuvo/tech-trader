package com.github.abusaeed_shuvo.techtrader.libs

import com.github.abusaeed_shuvo.techtrader.data.enums.FieldType

fun validateField(input: String, fieldType: FieldType): String? {
	return when (fieldType) {
		FieldType.USERNAME         -> if (input.isBlank()) {
			"Username can't be empty!"
		} else {
			null
		}

		FieldType.EMAIL            -> when {
			input.isBlank() -> "Email can't be blank!"
			else            -> null
		}

		FieldType.PASSWORD         -> when {
			input.isEmpty()                    -> "Password can't be empty!"
			input.length > MAX_PASSWORD_LENGTH -> "Password can't be longer than $MAX_PASSWORD_LENGTH!"
			else                               -> null
		}

		FieldType.PASSWORD_CONFIRM -> when {
			input.isEmpty()                    -> "Password can't be empty!"
			input.length > MAX_PASSWORD_LENGTH -> "Password can't be longer than $MAX_PASSWORD_LENGTH!"
			else                               -> null
		}
	}

}

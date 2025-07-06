package com.github.abusaeed_shuvo.techtrader

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.abusaeed_shuvo.techtrader.databinding.ActivityMainBinding
import com.github.abusaeed_shuvo.techtrader.viewmodel.TestViewModel

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding

	val viewModel: TestViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		enableEdgeToEdge()
		setContentView(binding.root)
		ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
			val systemBars =
				insets.getInsets(
					WindowInsetsCompat.Type.systemBars()
							or WindowInsetsCompat.Type.ime()
							or WindowInsetsCompat.Type.displayCutout()
				)
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
	}

	fun Activity.hideKeyboard(view: View) {
		val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
		imm?.hideSoftInputFromWindow(view.windowToken, 0)
	}

	override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
		if (ev.action == MotionEvent.ACTION_DOWN) {
			currentFocus?.let { focusedView ->
				if (focusedView is EditText) {
					val outRect = Rect()
					focusedView.getGlobalVisibleRect(outRect)
					if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
						focusedView.clearFocus()
						hideKeyboard(focusedView)
					}
				}
			}
		}
		return super.dispatchTouchEvent(ev)
	}
}
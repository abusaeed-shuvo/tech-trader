package com.github.abusaeed_shuvo.techtrader

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.abusaeed_shuvo.techtrader.databinding.ActivityMainBinding
import com.github.abusaeed_shuvo.techtrader.ui.notifications.NotificationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding


	private val requestNotificationPermissionLauncher =
		registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
			if (isGranted) {
				Toast.makeText(this, "Notification Permission granted!", Toast.LENGTH_SHORT).show()
			} else {
				Toast.makeText(this, "Notification permission denied!", Toast.LENGTH_SHORT).show()
			}

		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		enableEdgeToEdge()
		setContentView(binding.root)
		ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
			val systemBars = insets.getInsets(
				WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime() or WindowInsetsCompat.Type.displayCutout()
			)
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
		requestNotificationPermission()

		binding.notification.setOnClickListener {
			val route = Intent(this, NotificationActivity::class.java)
			startActivity(route)
		}
	}

	fun Activity.hideKeyboard(view: View) {
		val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
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

	fun requestNotificationPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			if (ContextCompat.checkSelfPermission(
					this, Manifest.permission.POST_NOTIFICATIONS
				) != PackageManager.PERMISSION_GRANTED
			) {
				requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
			}
		}
	}
}
package com.github.abusaeed_shuvo.techtrader

import android.app.Application
import android.os.Build
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
	override fun onCreate() {
		super.onCreate()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			DynamicColors.applyToActivitiesIfAvailable(this)
		}
	}
}
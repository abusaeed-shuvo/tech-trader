import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.google.gms.google.services)
}

android {
	namespace = "com.github.abusaeed_shuvo.techtrader"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.github.abusaeed_shuvo.techtrader"
		minSdk = 24
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_21
		targetCompatibility = JavaVersion.VERSION_21
	}
	buildFeatures {
		viewBinding = true
	}
	buildToolsVersion = "36.0.0"

}

kotlin {
	compilerOptions {
		jvmTarget.set(JvmTarget.JVM_21)
	}
}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.androidx.activity)
	implementation(libs.androidx.constraintlayout)
	implementation(libs.firebase.analytics)
	implementation(libs.firebase.auth)
	implementation(libs.androidx.credentials)
	implementation(libs.androidx.credentials.play.services.auth)
	implementation(libs.googleid)
	implementation(libs.androidx.navigation.fragment.ktx)
	implementation(libs.androidx.navigation.ui.ktx)
	implementation(libs.androidx.fragment)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)

	//lottie
	implementation(libs.lottie)

	//glide
	implementation(libs.glide)

	//ssp sdp
	implementation("com.intuit.ssp:ssp-android:1.1.1")
	implementation("com.intuit.sdp:sdp-android:1.1.1")

}
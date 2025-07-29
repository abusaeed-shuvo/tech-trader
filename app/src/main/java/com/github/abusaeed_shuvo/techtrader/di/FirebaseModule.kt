package com.github.abusaeed_shuvo.techtrader.di

import com.github.abusaeed_shuvo.techtrader.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

	@Provides
	@Singleton
	fun providesFirebaseAuth(): FirebaseAuth {
		return FirebaseAuth.getInstance()
	}

	@Provides
	@Singleton
	fun providesFirebase(auth: FirebaseAuth): AuthRepository {
		return AuthRepository(auth)
	}
}
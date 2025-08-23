package com.github.abusaeed_shuvo.techtrader.di

import com.github.abusaeed_shuvo.techtrader.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
	fun providesFireStore(): FirebaseFirestore {
		return FirebaseFirestore.getInstance()
	}


	@Provides
	@Singleton
	fun providesAuthRepository(auth: FirebaseAuth, db: FirebaseFirestore): AuthRepository {
		return AuthRepository(auth, db)
	}

	@Provides
	@Singleton
	fun providesFireBaseStorage(): StorageReference {
		return FirebaseStorage.getInstance().reference
	}


}
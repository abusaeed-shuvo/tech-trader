package com.github.abusaeed_shuvo.techtrader.di

import android.content.Context
import androidx.room.Room
import com.github.abusaeed_shuvo.techtrader.data.database.AppDatabase
import com.github.abusaeed_shuvo.techtrader.data.database.NotificationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

	@Provides
	@Singleton
	fun provideDatabase(@ApplicationContext context: Context): AppDatabase {

		return Room.databaseBuilder(
			context, AppDatabase::class.java, "tech_trader.db"
		).build()
	}

	@Provides
	fun provideNotificationDao(database: AppDatabase): NotificationDao {
		return database.notificationDao()
	}


}
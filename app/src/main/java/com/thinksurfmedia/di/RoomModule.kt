package com.thinksurfmedia.di

import android.content.Context
import com.thinksurfmedia.room.TSMDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = TSMDatabase(context)

    @Singleton
    @Provides
    fun provideHighlightDao(tsmDatabase: TSMDatabase) = tsmDatabase.highlightDao()

    @Singleton
    @Provides
    fun providePlanDao(tsmDatabase: TSMDatabase) = tsmDatabase.planDao()

    @Singleton
    @Provides
    fun providePortfolioDao(tsmDatabase: TSMDatabase) = tsmDatabase.portfolioDao()

    @Singleton
    @Provides
    fun provideReviewDao(tsmDatabase: TSMDatabase) = tsmDatabase.reviewDao()

    @Singleton
    @Provides
    fun provideServicesDao(tsmDatabase: TSMDatabase) = tsmDatabase.servicesDao()

}
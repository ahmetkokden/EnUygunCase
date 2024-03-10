package com.example.enuyguncase.di.module

import android.app.Application
import android.content.Context
import com.example.enuyguncase.data.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ProductService =
        retrofit.create(ProductService::class.java)
}
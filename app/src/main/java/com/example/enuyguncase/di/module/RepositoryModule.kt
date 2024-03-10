package com.example.enuyguncase.di.module

import com.example.enuyguncase.domain.repository.ProductRepository
import com.example.enuyguncase.domain.repository.ProductRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository
}
package com.example.enuyguncase.di.module

import com.example.enuyguncase.domain.usecase.ProductListSearchUseCase
import com.example.enuyguncase.domain.usecase.ProductListSearchUseCaseImpl
import com.example.enuyguncase.domain.usecase.ProductListUseCase
import com.example.enuyguncase.domain.usecase.ProductListUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun provideProductListSearchUseCase(productListSearchUseCaseImpl: ProductListSearchUseCaseImpl): ProductListSearchUseCase

    @Binds
    abstract fun provideProductListUseCase(productListUseCaseImpl: ProductListUseCaseImpl): ProductListUseCase
}
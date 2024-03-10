package com.example.enuyguncase.di.module

import com.example.enuyguncase.domain.usecase.ProductBasketUseCase
import com.example.enuyguncase.domain.usecase.ProductBasketUseCaseImpl
import com.example.enuyguncase.domain.usecase.ProductFavoriteUseCase
import com.example.enuyguncase.domain.usecase.ProductFavoriteUseCaseImpl
import com.example.enuyguncase.domain.usecase.ProductListSearchUseCase
import com.example.enuyguncase.domain.usecase.ProductListSearchUseCaseImpl
import com.example.enuyguncase.domain.usecase.ProductListUseCase
import com.example.enuyguncase.domain.usecase.ProductListUseCaseImpl
import com.example.enuyguncase.domain.usecase.ProductPaymentUseCase
import com.example.enuyguncase.domain.usecase.ProductPaymentUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun provideProductBasketUseCase(productBasketUseCaseImpl: ProductBasketUseCaseImpl): ProductBasketUseCase

    @Binds
    abstract fun provideProductFavoriteUseCase(productFavoriteUseCaseImpl: ProductFavoriteUseCaseImpl): ProductFavoriteUseCase

    @Binds
    abstract fun provideProductListSearchUseCase(productListSearchUseCaseImpl: ProductListSearchUseCaseImpl): ProductListSearchUseCase

    @Binds
    abstract fun provideProductListUseCase(productListUseCaseImpl: ProductListUseCaseImpl): ProductListUseCase

    @Binds
    abstract fun provideProductPaymentUseCase(productPaymentUseCaseImpl: ProductPaymentUseCaseImpl): ProductPaymentUseCase

}
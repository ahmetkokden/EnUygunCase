package com.example.enuyguncase.domain.usecase

import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.model.ProductListItem
import kotlinx.coroutines.flow.Flow

interface ProductListUseCase {

    suspend fun getProductList(): Flow<NetworkResult<ProductList>>
    suspend fun getProduct(
        id: Int
    ): Flow<NetworkResult<ProductListItem>>

    suspend fun getProductCategories(): Flow<NetworkResult<Array<String>>>
}
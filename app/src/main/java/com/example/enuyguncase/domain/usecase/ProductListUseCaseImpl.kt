package com.example.enuyguncase.domain.usecase

import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductListUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : ProductListUseCase {
    override suspend fun getProductList(): Flow<NetworkResult<ProductList>> =
        productRepository.getProductList()

    override suspend fun getProduct(id: Int): Flow<NetworkResult<ProductListItem>> =
        productRepository.getProduct(id)

    override suspend fun getProductCategories(): Flow<NetworkResult<Array<String>>> =
        productRepository.getProductCategories()

}
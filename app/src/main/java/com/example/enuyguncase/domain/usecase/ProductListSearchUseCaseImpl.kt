package com.example.enuyguncase.domain.usecase

import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductListSearchUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : ProductListSearchUseCase {
    override suspend fun searchProduct(searchKeyword: String): Flow<NetworkResult<ProductList>> = productRepository.searchProduct(searchKeyword)
}
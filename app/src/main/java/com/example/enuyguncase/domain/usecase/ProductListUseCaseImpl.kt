package com.example.enuyguncase.domain.usecase

import com.example.enuyguncase.domain.repository.ProductRepository
import javax.inject.Inject

class ProductListUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : ProductListUseCase {
}
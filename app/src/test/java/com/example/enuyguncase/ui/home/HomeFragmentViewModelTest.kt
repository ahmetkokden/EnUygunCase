package com.example.enuyguncase.ui.home

import com.example.enuyguncase.data.base.NetworkResult
import com.example.enuyguncase.domain.model.ProductList
import com.example.enuyguncase.domain.model.ProductListItem
import com.example.enuyguncase.domain.usecase.ProductListSearchUseCase
import com.example.enuyguncase.domain.usecase.ProductListUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeFragmentViewModelTest {

    @RelaxedMockK
    private lateinit var productListUseCase: ProductListUseCase

    @RelaxedMockK
    private lateinit var productListSearchUseCase: ProductListSearchUseCase

    lateinit var productList: ProductList
    lateinit var homeFragmentViewModel: HomeFragmentViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        productList = ProductList(
            listOf(
                ProductListItem(
                    id = 1,
                    brand = "",
                    title = "",
                    description = "",
                    displayFinalPrice = "",
                    displayPrice = "",
                    price = 1.0,
                    finalPrice = 1.0,
                    isFavorited = false,
                    discountPercentage = 1.0,
                    stock = 1,
                    category = "",
                    thumbnail = "",
                    productImages = listOf()
                )
            ), 1
        )
        homeFragmentViewModel =
            spyk(HomeFragmentViewModel(productListUseCase, productListSearchUseCase))
    }

    @Test
    fun productList_returnedResponseSuccess_calledGetProductList() {
        coEvery {
            productListUseCase.getProductList(
            )
        } returns flow {
            emit(
                NetworkResult(
                    NetworkResult.Status.SUCCESS,
                    productList, ""
                )
            )

        }

        lateinit var result: NetworkResult<ProductList>
        runBlocking {
            val flow = productListUseCase.getProductList()


            flow.collect {
                result = it
            }
        }


        TestCase.assertEquals(result.status, NetworkResult.Status.SUCCESS)
    }

    @Test
    fun productList_correctResponseBody_calledGetProductList(){

        coEvery {
            productListUseCase.getProductList(
            )
        } returns flow {
            emit(
                NetworkResult(
                    NetworkResult.Status.SUCCESS,
                    productList, ""
                )
            )

        }

        lateinit var result: NetworkResult<ProductList>
        runBlocking {
            val flow = productListUseCase.getProductList()


            flow.collect {
                result = it
            }
        }

        TestCase.assertEquals(result.data, productList)
    }
}
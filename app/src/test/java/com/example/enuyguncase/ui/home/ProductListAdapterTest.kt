package com.example.enuyguncase.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import com.example.enuyguncase.databinding.ItemProductBinding
import com.example.enuyguncase.domain.model.ProductListItem
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ProductListAdapterTest {

    @MockK(relaxed = true)
    lateinit var viewGroup: ViewGroup

    val productList = mutableListOf<ProductListItem>()
    private val productListItem = ProductListItem(
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

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        productList.add(
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
        )
    }

    @Test
    fun productListAdapterItemCount_EqualsItemCount_DataNotNull() {
        val adapter = spyk(ProductListAdapter(productList, clickListener = {

        }))
        every { adapter.notifyDataSetChanged() } answers {}
        adapter.updateItems(productList)

        assertEquals(adapter.itemCount, productList.size)
    }

    @Test
    fun productListAdapter_BindData_ViewHolderNotNull() {
        val adapter = spyk(ProductListAdapter(productList, clickListener = {

        }))
        every { adapter.notifyDataSetChanged() } answers {}
        adapter.updateItems(productList)
        val viewHolder =
            mockkClass(ProductListAdapter.ProductListAdapterViewHolder::class, relaxed = true)
        adapter.onBindViewHolder(viewHolder, 0)

        verify { viewHolder.bind(productList[0], 0) }
    }

    @Test
    fun productListAdapter_getCorrectItemPosition_getItemAtPosition() {
        val adapter = spyk(ProductListAdapter(productList, clickListener = {
        }))
        every { adapter.notifyDataSetChanged() } answers {}
        adapter.updateItems(productList)
        val viewHolder =
            mockkClass(ProductListAdapter.ProductListAdapterViewHolder::class, relaxed = true)
        adapter.onBindViewHolder(viewHolder, 0)

        assertEquals(
            viewHolder.adapterPosition,
            productList.indexOf(productListItem)
        )
    }


    @Test
    fun productListAdapter_bindingNotNull_calledBindFunction() {
        val adapter = spyk(ProductListAdapter(productList, clickListener = {
        }))
        every { adapter.notifyDataSetChanged() } answers {}
        adapter.updateItems(productList)
        val viewHolder =
            mockkClass(ProductListAdapter.ProductListAdapterViewHolder::class, relaxed = true)
        adapter.onBindViewHolder(viewHolder, 0)

        Assert.assertNotNull(viewHolder.binding)
    }

    @Test
    fun productListAdapter_notNull_createdViewHolder(){
        mockkStatic(ItemProductBinding::class)
        val inflater = mockk<LayoutInflater>(relaxed = true)
        val adapter = spyk(ProductListAdapter(productList, clickListener = {
        }))
        val viewGroup = mockk<ViewGroup>(relaxed = true)
        val context = mockk<Context>(relaxed = true)
        val itemBinding = mockk<ItemProductBinding>(relaxed = true)
        every { viewGroup.context } returns context
        every { ItemProductBinding.inflate(inflater) } returns itemBinding
        every { adapter.onCreateViewHolder(viewGroup,0) } returns adapter.ProductListAdapterViewHolder(itemBinding)
        val  viewHolder = adapter.ProductListAdapterViewHolder(itemBinding)
        adapter.onCreateViewHolder(viewGroup,0)

        Assert.assertNotNull(viewHolder.binding)

    }
    @Test
    fun productList_correctItemsSize_putProductList(){
        val adapter = spyk(ProductListAdapter(productList, clickListener = {
        }))
        adapter.items = productList

        assertEquals(adapter.items.size,productList.size)
    }

    @Test
    fun productList_isEmpty_calledUpdateItemsAsNull(){
        val adapter = spyk(ProductListAdapter(productList, clickListener = {
        }))
        every { adapter.notifyDataSetChanged() } answers {}

        adapter.updateItems(null)

        assert(adapter.items.isEmpty())
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}
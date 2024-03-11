package com.example.enuyguncase.ui.basket

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enuyguncase.R
import com.example.enuyguncase.data.local.BasketProductListEntity
import com.example.enuyguncase.databinding.ItemBasketProductBinding
import com.example.enuyguncase.utilities.format

class BasketProductListAdapter(
    private val productList: MutableList<BasketProductListEntity>,
    private val incProduct: (count: Int, productId: Long) -> Unit,
    private val descProduct: (count: Int, productId: Long) -> Unit,
    private val deleteProduct: (Int) -> Unit,
) :
    RecyclerView.Adapter<BasketProductListAdapter.BasketProductListAdapterViewHolder>() {
    var items: MutableList<BasketProductListEntity> = mutableListOf()

    init {
        items = productList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BasketProductListAdapterViewHolder {
        val binding = ItemBasketProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BasketProductListAdapterViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BasketProductListAdapterViewHolder, position: Int) {
        holder.bind(
            items[position],
            position
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(productList: MutableList<BasketProductListEntity>?) {
        items = productList ?: mutableListOf()
        notifyDataSetChanged()
    }

    inner class BasketProductListAdapterViewHolder(private val binding: ItemBasketProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var productList: BasketProductListEntity
        var position: Int? = null
        fun bind(productList: BasketProductListEntity, position: Int) {
            this.productList = productList
            this.position = position
            binding.tvProductDesc.setOnClickListener {
                descProduct(productList.productCount.dec(), productList.id)
            }

            binding.tvProductInc.setOnClickListener {
                incProduct(productList.productCount.inc(), productList.id)
            }

            binding.ivProductDelete.setOnClickListener {
                deleteProduct(productList.id.toInt())
            }

            if (productList.productCount == 1) {
                binding.tvProductDesc.isEnabled = false
            }

            if (productList.productCount == productList.stock) {
                binding.tvProductInc.isEnabled = false
            }

            binding.tvProductCount.text = productList.productCount.toString()
            binding.tvProductTitle.text = productList.title
            binding.tvProductPrice.text =
                binding.root.context?.getString(R.string.price, productList.price)
            binding.tvProductPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.tvProductFinalPrice.text =
                binding.root.context?.getString(R.string.price, productList.final_price.format())
            Glide.with(binding.root.context).load(productList.thumbnail)
                .into(binding.ivProductImage)
        }
    }
}
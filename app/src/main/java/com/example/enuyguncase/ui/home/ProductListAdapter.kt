package com.example.enuyguncase.ui.home

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.ItemProductBinding
import com.example.enuyguncase.domain.model.ProductListItem

class ProductListAdapter(private val productList: MutableList<ProductListItem>) :
    RecyclerView.Adapter<ProductListAdapter.ProductListAdapterViewHolder>() {
    var items: MutableList<ProductListItem> = mutableListOf()

    init {
        items = productList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductListAdapterViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductListAdapterViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ProductListAdapterViewHolder, position: Int) {
        holder.bind(
            items[position],
            position
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(productList: MutableList<ProductListItem>?) {
        items = productList ?: mutableListOf()
        notifyDataSetChanged()
    }

    inner class ProductListAdapterViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var productList: ProductListItem
        var position: Int? = null
        fun bind(productList: ProductListItem, position: Int) {
            this.productList = productList
            this.position = position
            binding.tvProductTitle.text = productList.title
            binding.tvProductInfo.text = productList.description
            binding.tvProductPrice.text =   binding.root.context?.getString(R.string.price, productList.displayPrice)
            binding.tvProductFinalPrice.text = binding.root.context?.getString(R.string.strike_price, productList.displayFinalPrice)
            binding.tvProductPrice.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
            Glide.with(binding.root.context).load(productList.thumbnail).into(binding.ivProductImage)
        }
    }
}
package com.example.enuyguncase.ui.favourite

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enuyguncase.R
import com.example.enuyguncase.data.local.FavoriteProductListEntity
import com.example.enuyguncase.databinding.ItemFavoriteProductBinding
import com.example.enuyguncase.domain.model.ProductListItem

class FavoriteProductListAdapter(
    private val productList: MutableList<FavoriteProductListEntity>,
    private val unFavoritedAction: (Int) -> Unit,
    private val addedBasket: (FavoriteProductListEntity) -> Unit
) :
    RecyclerView.Adapter<FavoriteProductListAdapter.FavoriteProductListAdapterViewHolder>() {
    var items: MutableList<FavoriteProductListEntity> = mutableListOf()

    init {
        items = productList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteProductListAdapterViewHolder {
        val binding = ItemFavoriteProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteProductListAdapterViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: FavoriteProductListAdapterViewHolder, position: Int) {
        holder.bind(
            items[position],
            position
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(productList: MutableList<FavoriteProductListEntity>?) {
        items = productList ?: mutableListOf()
        notifyDataSetChanged()
    }

    inner class FavoriteProductListAdapterViewHolder(private val binding: ItemFavoriteProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var productList: FavoriteProductListEntity
        var position: Int? = null
        fun bind(productList: FavoriteProductListEntity, position: Int) {
            this.productList = productList
            this.position = position
            binding.ivProductFavorite.setOnClickListener {
                productList.id.toInt().let { it1 -> unFavoritedAction(it1) }
            }
            binding.tvProductAdd.setOnClickListener {
                addedBasket(productList)
            }
            binding.tvProductTitle.text = productList.title
            binding.tvProductInfo.text = productList.description
            binding.tvProductPrice.text =
                binding.root.context?.getString(R.string.price, productList.price)
            Glide.with(binding.root.context).load(productList.thumbnail)
                .into(binding.ivProductImage)
        }
    }
}
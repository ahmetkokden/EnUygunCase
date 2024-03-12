package com.example.enuyguncase.ui.payment

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.ItemPaymentProductBinding
import com.example.enuyguncase.domain.model.PaymentProduct
import com.example.enuyguncase.utilities.format

class PaymentProductListAdapter(
    private val productList: MutableList<PaymentProduct>
) :
    RecyclerView.Adapter<PaymentProductListAdapter.PaymentProductListAdapterViewHolder>() {
    var items: MutableList<PaymentProduct> = mutableListOf()

    init {
        items = productList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentProductListAdapterViewHolder {
        val binding = ItemPaymentProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentProductListAdapterViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: PaymentProductListAdapterViewHolder, position: Int) {
        holder.bind(
            items[position],
            position
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(productList: MutableList<PaymentProduct>?) {
        items = productList ?: mutableListOf()
        notifyDataSetChanged()
    }

    inner class PaymentProductListAdapterViewHolder(private val binding: ItemPaymentProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var productList: PaymentProduct
        var position: Int? = null
        fun bind(productList: PaymentProduct, position: Int) {
            this.productList = productList
            this.position = position

            binding.tvProductCount.text =  binding.root.context?.getString(R.string.payment_product_count, productList.productCount.toString())
            binding.tvProductTitle.text = productList.title
            binding.tvProductFinalPrice.text =
                binding.root.context?.getString(R.string.price, productList.finalPrice.format())
            Glide.with(binding.root.context).load(productList.thumbnail)
                .into(binding.ivProductImage)
        }
    }
}
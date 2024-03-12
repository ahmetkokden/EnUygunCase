package com.example.enuyguncase.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.enuyguncase.R

class FilterAdapter(private val context: Context, private val categoryList:List<String>): ArrayAdapter<String>(context,
    R.layout.filter_list_item,categoryList) {

    private var selectedCategories = mutableListOf<String>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.filter_list_item,null)

        val categoryName = view.findViewById<TextView>(R.id.tv_category_name)
        val selectStatus = view.findViewById<ImageView>(R.id.iv_selected)

        categoryName.text = categoryList[position]

        selectStatus.setImageResource(if (selectedCategories.contains(categoryList[position])) R.drawable.ic_filled_dot else R.drawable.ic_empty_dot)

        return super.getView(position, convertView, parent)
    }
}
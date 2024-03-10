package com.example.enuyguncase.utilities

import android.content.Context
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat

fun TextView.setFont(context: Context, @FontRes font: Int) {
    try {
        typeface =
            ResourcesCompat.getFont(context, font)
    } catch (e: Exception) {
        //no-op
    }
}
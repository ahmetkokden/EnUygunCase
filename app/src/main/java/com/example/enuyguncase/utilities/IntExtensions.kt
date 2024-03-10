package com.example.enuyguncase.utilities

import android.content.Context
import java.text.DecimalFormat

fun Int.dpToPx(context: Context): Float = (this * context.resources.displayMetrics.density)

fun Double.format(): String {
    val dec = DecimalFormat("#.##")
    return dec.format(this)
}
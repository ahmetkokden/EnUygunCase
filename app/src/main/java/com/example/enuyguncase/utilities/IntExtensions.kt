package com.example.enuyguncase.utilities

import android.content.Context

fun Int.dpToPx(context: Context): Float = (this * context.resources.displayMetrics.density)
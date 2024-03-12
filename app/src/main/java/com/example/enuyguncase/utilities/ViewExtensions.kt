package com.example.enuyguncase.utilities

import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.os.CountDownTimer
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

fun TextView.setFont(context: Context, @FontRes font: Int) {
    try {
        typeface =
            ResourcesCompat.getFont(context, font)
    } catch (e: Exception) {
        //no-op
    }
}

fun Fragment.showAlertDialog(
    isSuccess: Boolean,
    title: String? = null,
    message: String,
    onFinishListener: (Boolean) -> Unit
) {
    val alertDialog = AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setTitle(message)
        .setPositiveButton("TAMAM"
        ) { dialog, which -> onFinishListener(isSuccess) }
        .create()

    alertDialog.show()
}
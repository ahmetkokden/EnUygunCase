package com.example.enuyguncase.ui.component

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.example.enuyguncase.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment : BottomSheetDialogFragment() {

    abstract fun bindView(inflater: LayoutInflater, container: ViewGroup?): View

    open val canDraggable = true
    abstract fun initUI()
    abstract fun initVMObservers()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return bindView(inflater, container)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initVMObservers()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener {
            val bottomSheet =
                (bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.apply {
                isDraggable = canDraggable
                isHideable = false
                state = BottomSheetBehavior.STATE_EXPANDED
                peekHeight = RelativeLayout.LayoutParams.WRAP_CONTENT
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) = Unit

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        if (slideOffset < 0.01f && state == BottomSheetBehavior.STATE_SETTLING) {
                            dismiss()
                        }
                    }
                })
            }

        }
        return bottomSheetDialog
    }
}
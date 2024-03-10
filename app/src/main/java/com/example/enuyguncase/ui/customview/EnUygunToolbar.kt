package com.example.enuyguncase.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.example.enuyguncase.R
import com.example.enuyguncase.databinding.EnuygunToolbarBinding
import com.example.enuyguncase.utilities.Constants.INT_ZERO
import com.example.enuyguncase.utilities.dpToPx
import com.example.enuyguncase.utilities.setFont
import java.util.Random

class EnUygunToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    protected val textView = TextView(context)

    var onLeftButtonClicked: (() -> Unit)? = null
    var onRightButtonClicked: (() -> Unit)? = null

    val binding: EnuygunToolbarBinding = EnuygunToolbarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        attrs.let {
            getContext().obtainStyledAttributes(attrs, R.styleable.EnUygunToolbar).apply {
                val leftIcon = getDrawable(R.styleable.EnUygunToolbar_leftIcon)
                setLeftIconVisibility(leftIcon != null)
                setLeftIcon(leftIcon)
                setRightIcon(
                    getDrawable(R.styleable.EnUygunToolbar_rightIcon)
                )
                setRightPadding(
                    getDimensionPixelSize(
                        R.styleable.EnUygunToolbar_rightIconPadding,
                        INT_ZERO
                    )
                )
                setLeftPadding(
                    getDimensionPixelSize(
                        R.styleable.EnUygunToolbar_leftIconPadding,
                        INT_ZERO
                    )
                )

                setTitle(getString(R.styleable.EnUygunToolbar_title))

                recycle()
            }
        }

        initTextView()
        setTextViewParams()

        binding.ivLeftIcon.setOnClickListener {
            onLeftButtonClicked?.invoke()
        }

        binding.ivRightIcon.setOnClickListener {
            onRightButtonClicked?.invoke()
        }
    }

    fun setTitle(text:String?){
        textView.text = text
    }

    private fun initTextView() {
        textView.gravity = Gravity.CENTER
        textView.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            resources.getDimension(R.dimen.unit14)
        )
        textView.setTextColor(ContextCompat.getColor(context, R.color.white))

        textView.setFont(context, R.font.Inter_Bold)
    }

    private fun setTextViewParams() {
        textView.id = Random().nextInt()
        val serviceNameParams = LayoutParams(
            0, 34.dpToPx(context).toInt()
        )
        textView.layoutParams = serviceNameParams
        binding.root.addView(textView)
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)
        constraintSet.connect(
            textView.getId(),
            ConstraintSet.START,
            binding.ivLeftIcon.getId(),
            ConstraintSet.END,
            12.dpToPx(context).toInt()
        );
        constraintSet.connect(
            textView.getId(),
            ConstraintSet.END,
            binding.ivRightIcon.id,
            ConstraintSet.START,
            6.dpToPx(context).toInt()
        );
        constraintSet.connect(
            textView.getId(),
            ConstraintSet.TOP,
            binding.root.getId(),
            ConstraintSet.TOP,
            0
        );
        constraintSet.connect(
            textView.getId(),
            ConstraintSet.BOTTOM,
            binding.root.getId(),
            ConstraintSet.BOTTOM,
            0
        );
        constraintSet.applyTo(binding.root)
    }

    private fun setRightPadding(padding: Int) {
        binding.ivRightIcon.setPadding(padding, padding, padding, padding)
    }

    private fun setLeftPadding(padding: Int) {
        binding.ivLeftIcon.setPadding(padding, padding, padding, padding)
    }

    private fun setRightIcon(rightIcon: Drawable?) {
        binding.ivRightIcon.setImageDrawable(rightIcon)
    }

    private fun setLeftIcon(leftIcon: Drawable?) {
        binding.ivLeftIcon.setImageDrawable(leftIcon)
    }

    private fun setLeftIconVisibility(leftIconEnabled: Boolean) {
        binding.ivLeftIcon.visibility = if (leftIconEnabled) VISIBLE else INVISIBLE
    }

}
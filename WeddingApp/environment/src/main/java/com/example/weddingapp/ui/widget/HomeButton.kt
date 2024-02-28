package com.example.weddingapp.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.weddingapp.R
import com.example.weddingapp.databinding.ItHomeButtonBinding

class HomeButton(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : ConstraintLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var binding: ItHomeButtonBinding =
        ItHomeButtonBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.HomeButton,
            0, 0
        ).apply {

            try {
                this@HomeButton.binding.apply {
                    tvLabel.apply {
                        text = getString(R.styleable.HomeButton_buttonLabel)
                        setTextColor(
                            getColor(
                                R.styleable.HomeButton_tint,
                                ContextCompat.getColor(context, R.color.white)
                            )
                        )
                    }
                    ivIcon.apply {
                        setPadding(
                            getDimension(
                                R.styleable.HomeButton_iconPadding,
                                16F,
                            ).toInt()
                        )
                        contentDescription = getString(R.styleable.HomeButton_contentDescription)
                        setImageDrawable(getDrawable(R.styleable.HomeButton_src))
                        setColorFilter(
                            getColor(
                                R.styleable.HomeButton_tint,
                                ContextCompat.getColor(context, R.color.black)
                            )
                        )
                    }
                }
            } finally {
                recycle()
            }
        }
    }
}
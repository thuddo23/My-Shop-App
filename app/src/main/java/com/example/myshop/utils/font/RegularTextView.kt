package com.example.myshop.utils.font

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class RegularTextView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val regularFont = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface = regularFont
    }
}
package com.example.myshop.utils.font

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class BoldTextView(context: Context, attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeFace = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = boldTypeFace

    }
}
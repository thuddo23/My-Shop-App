package com.example.myshop.utils.font

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton


class CustomRadioButton(context: Context, attributeSet: AttributeSet) : AppCompatRadioButton(context, attributeSet) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeFace = Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        typeface = boldTypeFace

    }
}
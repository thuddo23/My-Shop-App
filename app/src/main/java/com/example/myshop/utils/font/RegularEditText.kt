package com.example.myshop.font

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class RegularEditText(context: Context, attributeSet: AttributeSet) : AppCompatEditText(context, attributeSet) {
    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeFace = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        typeface = boldTypeFace
    }




}
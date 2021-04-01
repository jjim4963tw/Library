package com.jjim4963tw.library.utility

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan

class TextUtility {


    fun String.setBackground(color: Int, startIndex: Int, endIndex: Int): String {
        return SpannableStringBuilder(this).apply {
            this.setSpan(BackgroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }.toString()
    }


}
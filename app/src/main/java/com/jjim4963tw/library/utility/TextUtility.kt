package com.jjim4963tw.library.utility

import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.view.View

class TextUtility {

    fun String?.hasValue(): Boolean {
        return this != null && this != "" && this != "null" && this.isNotEmpty()
    }

    fun String.setBackground(color: Int, startIndex: Int, endIndex: Int): String {
        return SpannableStringBuilder(this).apply {
            this.setSpan(BackgroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }.toString()
    }
}
package com.example.baseproject.util.ext

import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.setTextColorRes(res: Int) {
    setTextColor(ContextCompat.getColor(context, res))
}
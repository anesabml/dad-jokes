package com.anesabml.dadjokes.extension

import android.util.DisplayMetrics
import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isVisible = false
}

fun View.showSnakeBar(text: String, length: Int = Snackbar.LENGTH_SHORT) =
    Snackbar.make(this, text, length).show()

fun View.convertDpToPixel(dp: Int): Int =
    dp * (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
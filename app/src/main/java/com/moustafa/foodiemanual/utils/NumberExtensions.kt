package com.moustafa.foodiemanual.utils

import android.content.res.Resources
import java.text.NumberFormat

/**
 * @author moustafasamhoury
 * created on Wednesday, 11 Sep, 2019
 */
/**
 * Returns the pixel representation of the number according to the device(From DP to PX)
 */
fun Int.px() =
    (this * Resources.getSystem().displayMetrics.density).toInt()

fun Long.formatted() = NumberFormat.getInstance().format(this)

fun Double.formatted() = NumberFormat.getInstance().format(this)

package com.moustafa.foodiemanual.ui.misc

/**
 * @author moustafasamhoury
 * created on Thursday, 19 Sep, 2019
 */


sealed class AsyncState<out T> {
    object Init : AsyncState<Nothing>()
    object Loading : AsyncState<Nothing>()
    data class Loaded<T>(val result: T) : AsyncState<T>()
    data class Failed(val failed: Throwable, val action: (() -> Any)? = null) : AsyncState<Nothing>()
}

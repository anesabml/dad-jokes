package com.anesabml.dadjokes.utils

sealed class Resources<out T> {
    object Loading : Resources<Nothing>()
    data class Success<out T>(val data: T) : Resources<T>()
    data class Error(val exception: Throwable) : Resources<Nothing>()
}
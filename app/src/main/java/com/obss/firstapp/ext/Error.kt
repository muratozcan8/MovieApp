package com.obss.firstapp.ext

import android.util.Log
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import java.net.UnknownHostException

fun <Key : Any, Value : Any> Throwable.toLoadResultError(
    errorMessage: MutableStateFlow<String>,
): PagingSource.LoadResult.Error<Key, Value> {
    when (this) {
        is UnknownHostException -> {
            errorMessage.value = "No internet connection"
            Log.e("network exception", "No internet connection", this)
        }
        is HttpException -> {
            errorMessage.value = "HTTP exception"
            Log.e("network exception", "HTTP exception", this)
        }
        else -> {
            errorMessage.value = "Unknown: ${this.message}"
            Log.e("network exception", "Unknown: ${this.message}", this)
        }
    }
    errorMessage.value = ""
    return PagingSource.LoadResult.Error(this)
}

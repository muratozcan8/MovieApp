package com.obss.firstapp.utils.ext

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
            errorMessage.value = "No Internet Connection"
            Log.e("network exception", "No Internet Connection", this)
        }
        is HttpException -> {
            errorMessage.value = "An error occurred while fetching movies"
            Log.e("network exception", "HTTP Exception", this)
        }
        else -> {
            errorMessage.value = "An error occurred while connecting to the server"
            Log.e("network exception", "Unknown: ${this.message}", this)
        }
    }
    errorMessage.value = ""
    return PagingSource.LoadResult.Error(this)
}

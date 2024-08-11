package com.obss.firstapp.ext

import android.content.Context

fun Int.pxToDp(context: Context): Float = this / context.resources.displayMetrics.density

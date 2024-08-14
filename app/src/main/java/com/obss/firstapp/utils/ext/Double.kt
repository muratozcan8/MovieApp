package com.obss.firstapp.utils.ext

import kotlin.math.roundToInt

fun Double.roundToSingleDecimal(): String = ((this * 10).roundToInt() / 10.0).toString()

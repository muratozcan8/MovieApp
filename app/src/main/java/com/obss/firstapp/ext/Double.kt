package com.obss.firstapp.ext

import kotlin.math.roundToInt

fun Double.roundToSingleDecimal(): String = ((this * 10).roundToInt() / 10.0).toString()

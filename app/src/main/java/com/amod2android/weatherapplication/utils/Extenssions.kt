package com.amod2android.weatherapplication

fun String.getTempString(): String {
    return this.substringBefore(".") + "°"
}












fun String.getHumidityString(): String {
    return "$this°"
}

package com.indianic.weatherapplication

fun String.getTempString(): String {
    return this.toString().substringBefore(".") + "°"
}

fun String.getHumidityString(): String {
    return this.toString() + "°"
}

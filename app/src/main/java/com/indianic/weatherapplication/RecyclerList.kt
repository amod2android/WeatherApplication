package com.indianic.weatherapplication

data class RecyclerList(val items: List<RecyclerData>)
data class RecyclerData(val time:String ,val day :String,val temp:String,val minTemp:String,val maxTemp:String)

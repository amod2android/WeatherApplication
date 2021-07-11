package com.indianic.weatherapplication

import android.app.Application


class MyApplication : Application() {
    private lateinit var retroComponent: RetroComponent

    override fun onCreate() {
        super.onCreate()

        retroComponent=DaggerRetroComponent.builder()
            .retroModule(RetroModule())
            .build()
    }



    fun gerRetroComponent():RetroComponent{
        return  retroComponent
    }
}

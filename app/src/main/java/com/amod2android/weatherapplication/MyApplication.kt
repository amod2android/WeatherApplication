package com.amod2android.weatherapplication

import android.app.Application
import com.amod2android.weatherapplication.network.DaggerRetroComponent
import com.amod2android.weatherapplication.network.RetroComponent
import com.amod2android.weatherapplication.network.RetroModule


class MyApplication : Application() {
    private lateinit var retroComponent: RetroComponent

    override fun onCreate() {
        super.onCreate()

        retroComponent= DaggerRetroComponent.builder()
            .retroModule(RetroModule())
            .build()
    }



    fun gerRetroComponent(): RetroComponent {
        return  retroComponent
    }
}

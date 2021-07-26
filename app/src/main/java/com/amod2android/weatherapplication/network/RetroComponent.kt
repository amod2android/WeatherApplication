package com.amod2android.weatherapplication.network

import com.amod2android.weatherapplication.ui.weather.CurrentWeatherFragment
import com.amod2android.weatherapplication.ui.weather.ForecastFragment
import com.amod2android.weatherapplication.ui.weather.WeatherInfoScreen
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface RetroComponent {

    fun inject(weatherInfoScreen: WeatherInfoScreen)
    fun inject(fragment: ForecastFragment)
    fun inject(fragment: CurrentWeatherFragment)
}
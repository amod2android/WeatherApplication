package com.indianic.weatherapplication

import com.indianic.weatherapplication.ui.weather.CurrentWeatherFragment
import com.indianic.weatherapplication.ui.weather.ForecastFragment
import com.indianic.weatherapplication.ui.weather.WeatherInfoScreen
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetroModule::class])
interface RetroComponent {

    fun inject(weatherInfoScreen: WeatherInfoScreen)
    fun inject(fragment: ForecastFragment)
    fun inject(fragment: CurrentWeatherFragment)
}
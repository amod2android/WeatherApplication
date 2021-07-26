package com.amod2android.weatherapplication.ui.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.amod2android.weatherapplication.MyApplication
import com.amod2android.weatherapplication.R


class WeatherInfoScreen : AppCompatActivity() {
    companion object{
        var lat: Double?=null
        var lng: Double?=null
        var address: String?=null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_info_screen)
        (application as MyApplication) .gerRetroComponent().inject(this)

        lat=intent.getDoubleExtra("LAT",0.0)
        lng=intent.getDoubleExtra("LNG",0.0)
        address=intent.getStringExtra("ADD")

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }
}
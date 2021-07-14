package com.indianic.weatherapplication.ui.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.indianic.weatherapplication.MyApplication
import com.indianic.weatherapplication.R


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
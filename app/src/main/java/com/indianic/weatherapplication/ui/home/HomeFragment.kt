package com.indianic.weatherapplication.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.indianic.weatherapplication.R
import com.indianic.weatherapplication.ui.weather.WeatherInfoScreen
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback {


    var lat: Double? = null
    var lng: Double? = null

    private var currentMarker: Marker? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentLocation: Location

    private lateinit var chooseBtn: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()
        chooseBtn = view.findViewById(R.id.chooseBtn)

        chooseBtn.setOnClickListener {
            startActivity(Intent(requireContext(), WeatherInfoScreen::class.java)
                .putExtra("LAT",lat)
                .putExtra("LNG",lng)
            )

        }
        return view
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1000
            )
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                this.currentLocation = location
                val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            1000 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                fetchLocation()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latlog = LatLng(currentLocation.latitude, currentLocation.latitude)
        drawMarker(latlog)

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker?) {
            }

            override fun onMarkerDrag(p0: Marker?) {

            }

            override fun onMarkerDragEnd(p0: Marker?) {
                if (currentMarker != null)
                    currentMarker?.remove()

                val newLatLng = LatLng(p0?.position!!.latitude, p0.position.longitude)
                drawMarker(newLatLng)
            }

        })
    }

    private fun drawMarker(latlng: LatLng) {
        val markerOptions = MarkerOptions().position(latlng).title("Selected Address")
            .snippet(getAddress(latlng.latitude, latlng.longitude)).draggable(true)

        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 8f))
        currentMarker = mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()

    }

    private fun getAddress(lat: Double, lng: Double): String {
        this.lat=lat
        this.lng=lng
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        return addresses[0].getAddressLine(0).toString()
    }
}
package com.indianic.weatherapplication.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.indianic.weatherapplication.LocationViewModel
import com.indianic.weatherapplication.R
import com.indianic.weatherapplication.databinding.FragmentHomeBinding
import com.indianic.weatherapplication.ui.weather.WeatherInfoScreen
import java.util.*


class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var latlng: LatLng
    private lateinit var binding: FragmentHomeBinding
    private var lat: Double? = null
    private var lng: Double? = null
    private var currentMarker: Marker? = null
    private var address: String? = null
    private lateinit var mMap: GoogleMap


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        startLocationUpdate()
        binding.chooseBtn.setOnClickListener {
            startActivity(Intent(requireContext(), WeatherInfoScreen::class.java)
                    .putExtra("LAT", lat)
                    .putExtra("LNG", lng)
                    .putExtra("ADD", address)
            )
        }
        return binding.root
    }

    private fun startLocationUpdate() {
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
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ), 1000
            )
            return
        }
        locationViewModel.getLocationData().observe(viewLifecycleOwner, {
            latlng = LatLng(it.latitude, it.longitude)
            val mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

        })
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
    ) {
        when (requestCode) {
            1000 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startLocationUpdate()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        drawMarker(latlng)
        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker?) {
            }

            override fun onMarkerDrag(marker: Marker?) {

            }

            override fun onMarkerDragEnd(marker: Marker?) {
                if (currentMarker != null)
                    currentMarker?.remove()
                 latlng = LatLng(marker?.position!!.latitude, marker.position.longitude)
                drawMarker(latlng)
            }
        })
    }

    private fun drawMarker(latlng: LatLng) {
        address=getAddress(latlng.latitude, latlng.longitude)
        val markerOptions = MarkerOptions().position(latlng).title("Selected Address")
                .snippet(address).draggable(true)
      //  mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 7f))
        currentMarker = mMap.addMarker(markerOptions)
        currentMarker?.showInfoWindow()
    }

    private fun getAddress(lat: Double, lng: Double): String {
        this.lat = lat
        this.lng = lng
        val geocode = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocode.getFromLocation(lat, lng, 1)
        return addresses[0].getAddressLine(0).toString()
    }
}
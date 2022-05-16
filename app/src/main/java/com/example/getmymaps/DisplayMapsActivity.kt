package com.example.getmymaps

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.getmymaps.databinding.ActivityDisplayMapsBinding
import com.example.getmymaps.models.UserMap
import com.google.android.gms.maps.model.LatLngBounds

class DisplayMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var userMap : UserMap
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityDisplayMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDisplayMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        userMap = intent.getSerializableExtra(EXTRA_USER_MAP) as UserMap
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        Log.i(TAG, "onMapReady: ${userMap.title}")
        val boundsBuilder = LatLngBounds.builder()
        for(place in userMap.places){
            val latlng = LatLng(place.latitude,place.longitude)
            boundsBuilder.include(latlng)
            mMap.addMarker(MarkerOptions().position(latlng).title(place.title).snippet(place.description))

        }
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),1000,1000,0))

    }
}
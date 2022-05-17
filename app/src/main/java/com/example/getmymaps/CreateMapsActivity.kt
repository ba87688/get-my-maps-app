package com.example.getmymaps

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.getmymaps.databinding.ActivityCreateMapsBinding
import com.example.getmymaps.models.Place
import com.example.getmymaps.models.UserMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.snackbar.Snackbar

class CreateMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCreateMapsBinding

    private var markers: MutableList<Marker> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = intent.getStringExtra(EXTRA_MAP_TITLE)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Snackbar.make(binding.root, "Long Press to add a marker! ", Snackbar.LENGTH_INDEFINITE)
            .setAction("OK",{})
            .setActionTextColor(ContextCompat.getColor(this,android.R.color.white))
            .show()
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

        mMap.setOnInfoWindowClickListener { markerToDelete ->
            markers.remove(markerToDelete)
            markerToDelete.remove()
        }

        mMap.setOnMapLongClickListener { latLng ->
            showAlertDialog(latLng)

        }
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun showAlertDialog(latLng: LatLng) {
        val placeFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_place,null)

        val dialog = AlertDialog.Builder(this).setTitle("create a marker")
            .setView(placeFormView)
            .setMessage("hello").setNegativeButton("Cancel", null)
            .setPositiveButton("OK", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = placeFormView.findViewById<EditText>(R.id.etTitle).text.toString()
            val description = placeFormView.findViewById<EditText>(R.id.etDescription).text.toString()
            if(title.trim().isEmpty() || description.trim().isEmpty()){
                Toast.makeText(this, "Enter title and description", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val marker = mMap.addMarker(MarkerOptions()
                .position(latLng).title(title).snippet(description))
            markers.add(marker)
            dialog.dismiss()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_create_map,menu)
        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.save_menu_button){
            Log.i(TAG, "onOptionsItemSelected: save button clicked.")
            //if list is empty, dont let them save because they can't
            if(markers.isEmpty()){
                Toast.makeText(this, "There has to be at least one marker to save", Toast.LENGTH_LONG).show()
                return true
            }
            val places = markers.map { marker ->
                Place(marker.title,marker.snippet,marker.position.latitude, marker.position.longitude)
            }
            val name = intent.getStringExtra(EXTRA_MAP_TITLE)
            val userMap = UserMap(name.toString(),places)
            val data = Intent()
            data.putExtra(EXTRA_USER_MAP,userMap)
            setResult(Activity.RESULT_OK,data)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
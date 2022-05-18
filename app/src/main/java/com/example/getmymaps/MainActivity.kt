package com.example.getmymaps

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getmymaps.adapter.MapsAdapter
import com.example.getmymaps.databinding.ActivityMainBinding
import com.example.getmymaps.models.Place
import com.example.getmymaps.models.UserMap
import com.google.android.gms.maps.model.MarkerOptions
import java.io.*

const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
const val EXTRA_MAP_TITLE = "EXTRA_MAP_TITLE"
const val TITLE_STRING = "TITLE_STRING"
private const val REQUEST_CODE = 1234
private const val FILENAME = "UserMaps.data"
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var userMaps: MutableList<UserMap>
    private lateinit var mapAdapter: MapsAdapter


//    //startactivityforresults alternative
//    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
//        ActivityResultCallback { result ->
//            if(result!=null && result.resultCode == RESULT_OK){
//                if(result.data != null ){
//
//                }
//
//            }
//
//        })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //read data from file and add them to the list
        userMaps = deserializeUserMaps(this).toMutableList()


        //layout manager of the recyclerview
        binding.rvMaps.layoutManager = LinearLayoutManager(this)
        // adapter for the recyclerview
        mapAdapter= MapsAdapter(this, userMaps, object : MapsAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick in main: $position")
                val intent = Intent(this@MainActivity, DisplayMapsActivity::class.java)
                intent.putExtra(EXTRA_USER_MAP, userMaps[position] )
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left)

            }

        })
        binding.rvMaps.adapter =mapAdapter


        binding.fabCreateMap.setOnClickListener{
            Log.i(TAG, "onCreate: TAPPING ON FAB")
            showAlertDialog()

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode== REQUEST_CODE && resultCode == Activity.RESULT_OK){
            var userMap = data?.getSerializableExtra(EXTRA_USER_MAP) as UserMap
            Log.i(TAG, "onActivityResult: ${userMap.title}")
            userMaps.add(userMap)
            mapAdapter.notifyItemInserted(userMaps.size -1)
            serializeUserMaps(this, userMaps)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun getDataFile(context: Context): File {
        Log.i(TAG, "getDataFile: ${context.filesDir}")
        return File(context.filesDir, FILENAME)

    }
    private fun serializeUserMaps(context: Context,userMap:List<UserMap>){
        Log.i(TAG, "serializeUserMaps: writing")
        ObjectOutputStream(FileOutputStream(getDataFile(context))).use {
            it.writeObject(userMap)
        }

    }
    private fun deserializeUserMaps(context: Context):List<UserMap>{
        Log.i(TAG, "deserializeUserMaps: ")
        val dataFile = getDataFile(context )
        if(!dataFile.exists()){
            Log.i(TAG, "data files does not exist yet")
            return emptyList()
        }
        ObjectInputStream(FileInputStream(dataFile)).use {
            return it.readObject() as List<UserMap>
        }
    }

    private fun showAlertDialog() {
        val mapFormView = LayoutInflater.from(this).inflate(R.layout.dialog_create_title,null)

        val dialog = AlertDialog.Builder(this).setTitle("create a marker")
            .setView(mapFormView)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK", null)
            .show()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = mapFormView.findViewById<EditText>(R.id.edit_text_title).text.toString()
            if(title.trim().isEmpty()){
                Toast.makeText(this, "Enter title and description", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val intent = Intent(this@MainActivity, CreateMapsActivity::class.java)
//            intent.putExtra(EXTRA_MAP_TITLE,"New Map Game!")
//            startForResult.launch(intent)
            startActivityForResult(intent, REQUEST_CODE)
            dialog.dismiss()
        }

    }


}

private fun generateSampleData(): List<UserMap> {
    return listOf(
        UserMap(
            "Memories from University",
            listOf(
                Place("Branner Hall", "Best dorm at Stanford", 37.426, -122.163),
                Place("Gates CS building", "Many long nights in this basement", 37.430, -122.173),
                Place("Pinkberry", "First date with my wife", 37.444, -122.170)
            )
        ),
        UserMap("January vacation planning!",
            listOf(
                Place("Tokyo", "Overnight layover", 35.67, 139.65),
                Place("Ranchi", "Family visit + wedding!", 23.34, 85.31),
                Place("Singapore", "Inspired by \"Crazy Rich Asians\"", 1.35, 103.82)
            )),
        UserMap("Singapore travel itinerary",
            listOf(
                Place("Gardens by the Bay", "Amazing urban nature park", 1.282, 103.864),
                Place("Jurong Bird Park", "Family-friendly park with many varieties of birds", 1.319, 103.706),
                Place("Sentosa", "Island resort with panoramic views", 1.249, 103.830),
                Place("Botanic Gardens", "One of the world's greatest tropical gardens", 1.3138, 103.8159)
            )
        ),
        UserMap("My favorite places in the Midwest",
            listOf(
                Place("Chicago", "Urban center of the midwest, the \"Windy City\"", 41.878, -87.630),
                Place("Rochester, Michigan", "The best of Detroit suburbia", 42.681, -83.134),
                Place("Mackinaw City", "The entrance into the Upper Peninsula", 45.777, -84.727),
                Place("Michigan State University", "Home to the Spartans", 42.701, -84.482),
                Place("University of Michigan", "Home to the Wolverines", 42.278, -83.738)
            )
        ),
        UserMap("Restaurants to try",
            listOf(
                Place("Champ's Diner", "Retro diner in Brooklyn", 40.709, -73.941),
                Place("Althea", "Chicago upscale dining with an amazing view", 41.895, -87.625),
                Place("Shizen", "Elegant sushi in San Francisco", 37.768, -122.422),
                Place("Citizen Eatery", "Bright cafe in Austin with a pink rabbit", 30.322, -97.739),
                Place("Kati Thai", "Authentic Portland Thai food, served with love", 45.505, -122.635)
            )
        )
    )


}
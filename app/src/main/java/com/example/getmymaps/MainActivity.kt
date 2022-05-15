package com.example.getmymaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getmymaps.adapter.MapsAdapter
import com.example.getmymaps.databinding.ActivityMainBinding
import com.example.getmymaps.models.UserMap

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //layout manager of the recyclerview
        binding.rvMaps.layoutManager = LinearLayoutManager(this)
        // adapter for the recyclerview
        binding.rvMaps.adapter = MapsAdapter(this, emptyList<UserMap>)
    }
}
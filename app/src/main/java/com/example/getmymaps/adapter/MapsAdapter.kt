package com.example.getmymaps.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.getmymaps.models.UserMap

class MapsAdapter(val conext: Context, val userMaps : List<UserMap>) : RecyclerView.Adapter<MapsAdapter.ViewHolder>(){
    class ViewHolder (itemView:View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}
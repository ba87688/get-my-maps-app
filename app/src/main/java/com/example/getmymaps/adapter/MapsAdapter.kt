package com.example.getmymaps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getmymaps.R
import com.example.getmymaps.models.UserMap

private const val TAG = "MapsAdapter"

class MapsAdapter(val conext: Context, val userMaps : List<UserMap>, val onClickListener: OnClickListener) : RecyclerView.Adapter<MapsAdapter.ViewHolder>(){

    //create interface
    interface OnClickListener{
        fun onItemClick(position: Int)
    }

    class ViewHolder (itemView:View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(conext).inflate(R.layout.item_map_two,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = userMaps[position]
        holder.itemView.setOnClickListener {
            onClickListener.onItemClick(position)

        }
        val textViewTitle = holder.itemView.findViewById<TextView>(R.id.tvMaptitle)
        val textViewNumber = holder.itemView.findViewById<TextView>(R.id.tvMapNum)
        textViewTitle.text = current.title
        textViewNumber.text = current.places.size.toString()

    }

    override fun getItemCount(): Int {
        return userMaps.size

    }

}
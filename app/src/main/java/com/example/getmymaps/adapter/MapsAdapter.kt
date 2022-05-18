package com.example.getmymaps.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getmymaps.R
import com.example.getmymaps.models.UserMap
import java.util.ArrayList

private const val TAG = "MapsAdapter"

class MapsAdapter(
    val conext: Context,
    val userMaps: List<UserMap>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<MapsAdapter.ViewHolder>(), Filterable {

    val exampleList: MutableList<UserMap> = userMaps.toMutableList()
    val exampleList1: List<UserMap> = exampleList.toMutableList()

    //create interface
    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(conext).inflate(R.layout.item_map_two, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = exampleList[position]
        holder.itemView.setOnClickListener {
            onClickListener.onItemClick(position)

        }
        val textViewTitle = holder.itemView.findViewById<TextView>(R.id.tvMaptitle)
        val textViewNumber = holder.itemView.findViewById<TextView>(R.id.tvMapNum)
        textViewTitle.text = current.title
        textViewNumber.text = current.places.size.toString()

    }

    override fun getItemCount(): Int {
        return exampleList.size

    }

    override fun getFilter(): Filter {
        return exampleFilter
    }

    //    val exampleFilter:Filter =Filter
    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<UserMap?> = ArrayList()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(exampleList1)
            } else {
                val filterPattern = constraint.toString().lowercase().trim { it <= ' ' }
                for (item in exampleList1) {
                    if (item!!.title.lowercase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            exampleList.clear()
            exampleList.addAll(results.values as MutableList<UserMap>)
            notifyDataSetChanged()
        }
    }
}
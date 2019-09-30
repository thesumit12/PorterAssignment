package com.example.porterassignment.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.porterassignment.R
import com.example.porterassignment.model.PlaceModel
import com.example.porterassignment.ui.view.PlaceClickListener
import com.google.android.libraries.places.api.model.Place
import kotlinx.android.synthetic.main.search_item.view.*

class SelectPlaceAdapter(private val context: Context, private val placeList: List<PlaceModel>,
                         private val listener: PlaceClickListener)
    : RecyclerView.Adapter<SelectPlaceAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
         MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.search_item, parent, false))

    override fun getItemCount(): Int = placeList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = placeList[position].name
        holder.address.text = placeList[position].address
        holder.item.setOnClickListener {
            listener.onPlaceSelected(position)
        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.tv_name
        val address: TextView = view.tv_address
        val item: ConstraintLayout = view.item
    }
}
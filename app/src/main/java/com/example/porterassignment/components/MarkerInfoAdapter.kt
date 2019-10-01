package com.example.porterassignment.components

import android.view.LayoutInflater
import android.view.View
import com.example.porterassignment.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.custom_info_window.view.*

class MarkerInfoAdapter(private val layoutInflater: LayoutInflater) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker?): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker?): View {
        val view = layoutInflater.inflate(R.layout.custom_info_window, null)

        view.tv_price.text = marker?.title
        view.tv_eta.text = marker?.snippet

        return view
    }
}
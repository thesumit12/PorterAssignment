package com.example.porterassignment.model

data class PlaceModel(
    var placeId: String = "",
    var name: String = "",
    var address: String = "",
    var lat: Double? = 0.0,
    var lang: Double? = 0.0
)
package com.example.porterassignment.repository

import androidx.lifecycle.LiveData
import com.example.porterassignment.apiManager.ApiManager
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class VehicleRepository(private val apiManager: ApiManager) {

    suspend fun getServiceability(): Boolean = apiManager.checkServiceability()

    suspend fun getCost(lat: Double, lng: Double) = apiManager.getCost(lat, lng)

    suspend fun getEta(lat: Double, lng: Double) = apiManager.getEta(lat, lng)
}
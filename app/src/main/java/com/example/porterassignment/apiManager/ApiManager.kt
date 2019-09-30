package com.example.porterassignment.apiManager

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.porterassignment.model.response.ETA
import com.example.porterassignment.model.response.ServiceabilityResponse
import com.example.porterassignment.model.response.VehicleCost
import com.example.porterassignment.network.IApiService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.Exception

@ExperimentalCoroutinesApi
open class ApiManager(private val apiService: IApiService) {
    private val mTAG = ApiManager::class.java.simpleName

    open suspend fun checkServiceability(): Boolean {
        val request = apiService.getServiceabilityStatus()
        val result = executeNetworkCall(request) as ServiceabilityResponse
        return result.serviceable
    }

    open suspend fun getCost(lat: Double, lng: Double): String {
        val request = apiService.getCost(lat, lng)
        val result = executeNetworkCall(request) as VehicleCost
        return result.cost
    }

    open suspend fun getEta(lat: Double, lng: Double): String {
        val request = apiService.getEta(lat, lng)
        val result = executeNetworkCall(request) as ETA
        return result.eta
    }

    @ExperimentalCoroutinesApi
    private suspend fun <T> executeNetworkCall(call: Deferred<T>) = try {
        call.join()
        call.getCompleted()
    }catch (ex: Exception) {
        Log.e(mTAG, "Exception during network operation -> ${ex.printStackTrace()}")
    }
}
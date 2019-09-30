package com.example.porterassignment.network

import com.example.porterassignment.model.response.ETA
import com.example.porterassignment.model.response.ServiceabilityResponse
import com.example.porterassignment.model.response.VehicleCost
import retrofit2.http.GET
import kotlinx.coroutines.Deferred
import retrofit2.http.Query

/**
 * Interface for APi end points
 * @author Sumit Lakra
 * @date 09/02/19
 */
interface IApiService {

    @GET("users/serviceability")
    fun getServiceabilityStatus(): Deferred<ServiceabilityResponse>

    @GET("vehicles/cost")
    fun getCost(@Query("lat") lat: Double,
                @Query("lng") lang: Double): Deferred<VehicleCost>

    @GET("vehicles/eta")
    fun getEta(@Query("lat") lat: Double,
               @Query("lng") lang: Double): Deferred<ETA>
}
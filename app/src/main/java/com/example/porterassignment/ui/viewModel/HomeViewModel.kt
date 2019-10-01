package com.example.porterassignment.ui.viewModel

import android.location.Location
import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.porterassignment.components.BaseViewModel
import com.example.porterassignment.model.EventIdentifier
import com.example.porterassignment.repository.VehicleRepository
import kotlinx.coroutines.*
import java.lang.Runnable

class HomeViewModel @ExperimentalCoroutinesApi constructor(val repository: VehicleRepository): BaseViewModel() {

    var mLastLocation: Location? = null
    val pickUpLocation = ObservableField<String>("From")
    val dropLocation = ObservableField<String>("Destination")
    val isServiceable = ObservableBoolean(true)
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var latitude: Double? = 0.0
    var longitude: Double? = 0.0
    var cost: String = ""
    var eta: String = ""
    val waitingDialogMsg = MutableLiveData<String>()
    private val handler = Handler()

    init {
        handler.postDelayed(object : Runnable {
            override fun run() {
                uiScope.launch {
                    withContext(Dispatchers.IO) {
                        isServiceable.set(repository.getServiceability())
                    }
                }
                handler.postDelayed(this, 20000)
            }
        }, 0)
    }

    fun selectSourceClicked() {
        triggerEvent(EventIdentifier.SELECT_SOURCE)
    }

    fun selectDestinationClicked() {
        triggerEvent(EventIdentifier.SELECT_DESTINATION)
    }

    fun getCost() {
        waitingDialogMsg.value = "Getting result"
        uiScope.launch {
            cost = repository.getCost(latitude ?: 0.0, longitude ?: 0.0)
            Log.e("HVM","Cost $cost")
            waitingDialogMsg.value = null
        }
    }

    fun getEta() {
        waitingDialogMsg.value = "Getting result"
        uiScope.launch {
            eta = repository.getEta(latitude ?: 0.0, longitude ?: 0.0)
            Log.e("ETA"," $eta")
            waitingDialogMsg.value = null
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
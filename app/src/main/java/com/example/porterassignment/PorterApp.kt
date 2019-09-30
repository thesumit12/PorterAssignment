package com.example.porterassignment

import android.app.Application
import com.example.porterassignment.di.appModule
import com.google.android.libraries.places.api.Places
import org.koin.android.ext.android.startKoin
import java.util.*

/**
 * @brief: Class that extends Application class. It contains all the application level initializations.
 * @author slakra
 * @date 09/02/19
 */
class PorterApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key), Locale.US)
        }
    }

    private fun initKoin() {
        startKoin(this, appModule)
    }
}
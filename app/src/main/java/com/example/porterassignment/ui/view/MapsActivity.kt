package com.example.porterassignment.ui.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.porterassignment.BR
import com.example.porterassignment.R
import com.example.porterassignment.common.util.Constant
import com.example.porterassignment.common.util.toast
import com.example.porterassignment.components.BaseActivity
import com.example.porterassignment.components.MarkerInfoAdapter
import com.example.porterassignment.databinding.ActivityMapsBinding
import com.example.porterassignment.model.EventIdentifier
import com.example.porterassignment.ui.viewModel.HomeViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.androidx.viewmodel.ext.android.viewModel

class MapsActivity : BaseActivity<ActivityMapsBinding, HomeViewModel>(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val mViewModel: HomeViewModel by viewModel()
    private var mLocationPermissionGranted = false
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setActionBar()
        subscribeObservers()
    }

    private fun setActionBar() {
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar)
    }

    override fun getLayoutId(): Int = R.layout.activity_maps

    override fun getViewModel(): HomeViewModel = mViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (!mLocationPermissionGranted) {
            checkLocationPermission()
        }else {
            updateUi()
            getCurrentLocation()
        }

        mMap.setInfoWindowAdapter(MarkerInfoAdapter(layoutInflater))

    }

    private fun subscribeObservers() {
        mViewModel.onEventReceived += {event ->
            when(event.type) {
                EventIdentifier.SELECT_SOURCE -> {
                    val intent = Intent(this@MapsActivity, SelectPlaceActivity::class.java)
                    intent.putExtra(Constant.KEY_SOURCE, true)
                    startActivityForResult(intent, Constant.SRC_SELECTION)
                }
                EventIdentifier.SELECT_DESTINATION -> {
                    val intent = Intent(this@MapsActivity, SelectPlaceActivity::class.java)
                    intent.putExtra(Constant.KEY_SOURCE, false)
                    startActivityForResult(intent, Constant.DEST_SELECTION)
                }
                else -> {
                    //Do nothing
                }
            }

        }

        mViewModel.waitingDialogMsg.observe(this, Observer {msg->
            if (msg != null) {
                showWaitingDialog("")
            }else{
                hideWaitingDialog()
                showMarker()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val name = data?.getStringExtra(Constant.KEY_NAME)
        val address = data?.getStringExtra(Constant.KEY_ADDRESS)
        mViewModel.latitude = data?.getDoubleExtra(Constant.KEY_LAT, 0.0)
        mViewModel.longitude = data?.getDoubleExtra(Constant.KEY_LNG, 0.0)
        mViewModel.getCost()
        mViewModel.getEta()
        when(requestCode){
            Constant.SRC_SELECTION -> {
                mViewModel.pickUpLocation.set("$name,$address")
            }
            Constant.DEST_SELECTION -> {
                mViewModel.dropLocation.set("$name,$address")
            }
            else -> {

            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            updateUi()
            getCurrentLocation()
        }else {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                Constant.PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            Constant.PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                    updateUi()
                    getCurrentLocation()
                }else{
                    toast("Location permission is required to use this application")
                    this.finish()
                }
            }
            else -> {
                //Do nothing
            }
        }
    }

    private fun getCurrentLocation() {
        try {
            if (mLocationPermissionGranted) {
                val locationRequest = mFusedLocationProviderClient.lastLocation
                locationRequest.addOnCompleteListener{task->
                    if (task.isSuccessful) {
                        mViewModel.mLastLocation = task.result
                        mMap.addMarker(MarkerOptions()
                            .position(
                                LatLng(mViewModel.mLastLocation!!.latitude,
                                    mViewModel.mLastLocation!!.longitude))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_pin)))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            LatLng(mViewModel.mLastLocation!!.latitude,
                                mViewModel.mLastLocation!!.longitude), 15F
                        ))
                    }else {
                        //show default location
                        val sydney = LatLng(-34.0, 151.0)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            sydney, 15F
                        ))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        }catch (e: SecurityException) {
            Log.e(TAG, e.localizedMessage)
        }
    }

    private fun updateUi() {
        if (mLocationPermissionGranted) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
        }else {
            mMap.isMyLocationEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false
            mViewModel.mLastLocation = null
            checkLocationPermission()
        }
    }

    private fun showMarker() {
        mMap.addMarker(MarkerOptions()
            .position(LatLng(mViewModel.latitude ?: 0.0, mViewModel.longitude ?: 0.0))
            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_pin))
            .title(mViewModel.cost)
            .snippet("${mViewModel.eta} mins")
        ).showInfoWindow()

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
            LatLng(mViewModel.latitude ?: 0.0, mViewModel.longitude ?: 0.0), 15F
        ))
    }

    companion object {
        private val TAG = MapsActivity::class.java.simpleName
    }
}

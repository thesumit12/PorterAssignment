package com.example.porterassignment.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.porterassignment.BR
import com.example.porterassignment.R
import com.example.porterassignment.common.util.Constant
import com.example.porterassignment.common.util.toast
import com.example.porterassignment.components.BaseActivity
import com.example.porterassignment.databinding.ActivitySelectPlaceBinding
import com.example.porterassignment.model.EventIdentifier
import com.example.porterassignment.model.PlaceModel
import com.example.porterassignment.ui.view.adapter.SelectPlaceAdapter
import com.example.porterassignment.ui.viewModel.SelectPlaceViewModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.activity_select_place.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class SelectPlaceActivity : BaseActivity<ActivitySelectPlaceBinding, SelectPlaceViewModel>(),
    PlaceClickListener{
    private val mViewModel: SelectPlaceViewModel by viewModel()
    private var selectSource = true
    private lateinit var mAdapter: SelectPlaceAdapter
    private val placeList: ArrayList<PlaceModel> = ArrayList()
    private lateinit var placesClient: PlacesClient
    private var mToken: AutocompleteSessionToken? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectSource = intent?.extras?.getBoolean(Constant.KEY_SOURCE) ?: true
        setActionBar()
        subscribeObservers()
        placesClient = Places.createClient(this)
        mViewModel.setPropertyChangeCallback()
        initializeRecyclerView()
    }

    override fun getLayoutId(): Int = R.layout.activity_select_place

    override fun getViewModel(): SelectPlaceViewModel = mViewModel

    override fun getBindingVariable(): Int = BR.viewModel

    private fun setActionBar() {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (selectSource) {
            getString(R.string.select_source)
        } else {
            getString(R.string.select_destination)
        }
    }

    private fun subscribeObservers() {
        mViewModel.onEventReceived += {event ->
            when(event.type) {
                EventIdentifier.SEARCH_STRING_CHANGED -> getPlacesList()
                else -> {

                }
            }
        }
    }

    private fun initializeRecyclerView() {
        mAdapter = SelectPlaceAdapter(this, placeList, this)
        val mLayoutManager = LinearLayoutManager(this)
        places_rv.layoutManager = mLayoutManager
        places_rv.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        places_rv.adapter = mAdapter
    }

    private fun getPlacesList() {
        if (mToken == null) {
            mToken = AutocompleteSessionToken.newInstance()
        }

        val request = FindAutocompletePredictionsRequest.builder()
            .setCountry("in")
            .setTypeFilter(TypeFilter.REGIONS)
            .setSessionToken(mToken)
            .setQuery(mViewModel.searchString.get())
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener{response->
            placeList.clear()
            for(prediction in response.autocompletePredictions) {
                placeList.add(PlaceModel(
                    prediction.placeId,
                    prediction.getPrimaryText(null).toString(),
                    prediction.getSecondaryText(null).toString()))
            }
            mAdapter.notifyDataSetChanged()
        }.addOnFailureListener{exception->
            Log.e("Exception", exception.localizedMessage)
        }
    }

    override fun onPlaceSelected(position: Int) {
        mToken = null
        val place = placeList[position]
        val intent = Intent()
        intent.putExtra(Constant.KEY_NAME, place.name)
        intent.putExtra(Constant.KEY_ADDRESS, place.address)
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.builder(place.placeId, placeFields).build()
        placesClient.fetchPlace(request).addOnSuccessListener {response->
            val location = response.place.latLng
            Log.e("Sumit","Place ${response.place}")
            Log.e("Sumit","${location?.latitude},${location?.longitude}")
            intent.putExtra(Constant.KEY_LAT, location?.latitude)
            intent.putExtra(Constant.KEY_LNG, location?.longitude)
            setResult(intent)
        }.addOnFailureListener { ex->
            Log.e("SelectPlaceActivity", "Exception ${ex.localizedMessage}")
            setResult(intent)
        }
    }

    private fun setResult(intent: Intent) {
        if (selectSource) {
            setResult(Constant.SRC_SELECTION, intent)
        }else {
            setResult(Constant.DEST_SELECTION, intent)
        }
        finish()
    }

}

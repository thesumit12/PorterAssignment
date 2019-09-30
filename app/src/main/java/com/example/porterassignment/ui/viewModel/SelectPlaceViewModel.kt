package com.example.porterassignment.ui.viewModel

import androidx.databinding.ObservableField
import com.example.porterassignment.components.BaseViewModel
import com.example.porterassignment.model.EventIdentifier

class SelectPlaceViewModel: BaseViewModel() {

    val searchString = ObservableField<String>("")

    fun setPropertyChangeCallback() {
        searchString.addOnPropertyChanged {
            triggerEvent(EventIdentifier.SEARCH_STRING_CHANGED, "")
        }
    }
}
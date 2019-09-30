package com.example.porterassignment.components

import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import com.example.porterassignment.common.Event
import com.example.porterassignment.model.EventIdentifier
import com.example.porterassignment.model.EventType
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @brief: BaseviewModel class for all viewModels to extend
 * @author: Sumit Lakra
 * @date: 09/02/2019
 */
open class BaseViewModel : ViewModel() {

    val onEventReceived: Event<EventType> = Event()
    private val disposable = CompositeDisposable()

    /**
     * Function to be used by child view models to trigger any event
     * @author: Sumit Lakra
     * @date: 09/02/2019
     */
    fun triggerEvent(type: EventIdentifier, dataObj: Any = "") {
        val eventType = EventType(type, dataObj)
        onEventReceived(eventType)
    }

    /**
     * define a property change callback which calls "callback " on change
     * @return Unit
     * @author: Sumit Lakra
     * @date: 09/02/2019
     *
     * */
    @Suppress("UNCHECKED_CAST")
    fun <T : Observable> T.addOnPropertyChanged(callback: (T) -> Unit) =
        object : Observable.OnPropertyChangedCallback() {
            @Suppress("UNCHECKED_CAST")
            override fun onPropertyChanged(observable: Observable?, i: Int) =
                callback(observable as T)
        }.also { addOnPropertyChangedCallback(it) }

    /**
     * Function to add disposable'
     * @author: Sumit Lakra
     * @date: 09/02/2019
     */
    fun addDisposable(d: Disposable) {
        disposable.add(d)
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}
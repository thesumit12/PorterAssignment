package com.example.porterassignment.common.eventBus

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
@file_name: RxBus.kt
@author: Sumit Lakra
@brief: RxBus will serve as event bus for communication between two decoupled components
@date: 04/26/2019
 */
object RxBus {

    private val publisher = PublishSubject.create<Any>()

    /**
     * Will post message event over bus
     * @param event
     * @author Sumit Lakra
     * @date: 04/26/2019
     */
    fun publish(event: Any) {
        publisher.onNext(event)
    }

    /**
     * Will keep list of all subscribers for a given event
     * @param eventType
     * @author Sumit Lakra
     * @date: 04/26/2019
     */
    fun <T> listen(eventType: Class<T>): Observable<T> = publisher.ofType(eventType)
}
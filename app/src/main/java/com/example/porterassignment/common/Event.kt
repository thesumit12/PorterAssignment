package com.example.porterassignment.common

/**
 * Class that defines reusable component Event used throughout the app.
 * @author: Sumit Lakra
 * @date: 09/02/2019
 */
class Event<T> {

    private val handlers = arrayListOf<(Event<T>.(T) -> Unit)>()

    /**
     * Function to overload '+' operator to add new event.
     * @author Sumit Lakra
     * @param [handler] Event of any generic type
     * @date 09/02/2019
     */
    operator fun plusAssign(handler: Event<T>.(T) -> Unit) {
        handlers.add(handler)
    }

    /**
     * Function to overload '-' operator to remove event.
     * @author Sumit Lakra
     * @param [handler] Event of any generic type
     * @date 09/02/2019
     */
    operator fun minusAssign(handler: Event<T>.(T) -> Unit) {
        handlers.remove(handler)
    }

    /**
     * Function to overload '[]' operator to invoke any particular event.
     * @author Sumit Lakra
     * @param [value] Any generic type
     * @date 09/02/2019
     */
    operator fun invoke(value: T) {
        for (handler in handlers) handler(value)
    }

    /**
     * Function to remove all events.
     * @author Sumit Lakra
     * @date 09/02/2019
     */
    fun removeAllHandlers() {
        handlers.clear()
    }
}
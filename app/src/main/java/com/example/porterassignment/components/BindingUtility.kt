package com.example.porterassignment.components

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

/**
 * Binding function to set [String] error message
 * @author Sumit Lakra
 * @param [view] TextInputLayout
 * @param [errorMessage] String
 * @date 04/25/2019
 */
@BindingAdapter(value = ["errorText"])
fun setErrorMessage(view: TextInputLayout, errorMessage: String?) {
    view.error = errorMessage
}

/**
 * Binding function to set [Int] error message
 * @author Sumit Lakra
 * @param [view] TextInputLayout
 * @param [errorMessage] Int
 * @date 04/25/2019
 */
@BindingAdapter(value = ["errorText"])
fun setErrorMessageInt(view: TextInputLayout, errorMessage: Int?) {
    view.error = errorMessage?.let { view.context.getString(errorMessage) }
}
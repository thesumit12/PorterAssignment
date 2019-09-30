package com.example.porterassignment.common.util

import android.content.Context
import android.widget.Toast

/**
 * Extension function to show toast
 * @param [msg] String
 * @author slakra
 * @date 09/04/19
 */
fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
}
package com.codehustlers.bankaks.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    fun showKeyboard(activity: Activity?) {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager?.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(activity: Activity?) {
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (activity.currentFocus != null) {
            inputManager.hideSoftInputFromWindow(activity.currentFocus !!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun hideKeyboard(focusView: View?) {
        val imm = focusView?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(focusView?.windowToken, 0)
    }

    fun hideKeyboard(activity: Activity?, view: View?) {
        val inputManager = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
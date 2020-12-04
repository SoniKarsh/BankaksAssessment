package com.codehustlers.bankaks.utils

import android.widget.AdapterView
import android.widget.Spinner
import androidx.databinding.BindingAdapter

object AppBinding {
    @JvmStatic
    @BindingAdapter("app:OnItemSelected")
    fun OnItemSelected(spinner: Spinner, listener: AdapterView.OnItemSelectedListener) {
        spinner.onItemSelectedListener = listener
    }
}
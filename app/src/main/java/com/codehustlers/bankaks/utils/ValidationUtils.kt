package com.codehustlers.bankaks.utils

import android.util.Log
import com.codehustlers.bankaks.R
import java.util.regex.Pattern

object ValidationUtils {

    fun isFieldValid(regex: String, data: String?): Pair<Boolean, Int> {

        try {
            val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)

            if (data.isNullOrEmpty()) {
                return Pair(false, R.string.error_some_data)
            } else if (regex.isNotEmpty() && !pattern.matcher(data).matches()) {
                return Pair(false, R.string.error_valid_data)
            }
            return Pair(true, 0)
        } catch (e: Exception) {
            Log.e("err", e.message.toString())
            return Pair(false, R.string.exception_occurred)
        }
    }

}
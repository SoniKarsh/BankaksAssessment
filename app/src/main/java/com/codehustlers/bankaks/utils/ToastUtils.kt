package com.codehustlers.bankaks.utils

import android.widget.Toast
import com.codehustlers.bankaks.App

object ToastUtils {

    fun shortToast(stringCode: Int = 0, stringText: String? = null) {
        if (App.instance != null) {
            if (stringCode != 0) {
                Toast.makeText(App.instance, App.instance?.getText(stringCode), Toast.LENGTH_SHORT).show()
            } else if (stringText != null) {
                Toast.makeText(App.instance, stringText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun longToast(stringCode: Int = 0, stringText: String? = null) {
        if (App.instance != null) {
            if (stringCode != 0) {
                Toast.makeText(App.instance, App.instance?.getText(stringCode), Toast.LENGTH_LONG).show()
            } else if (stringText != null) {
                Toast.makeText(App.instance, stringText, Toast.LENGTH_LONG).show()
            }
        }
    }

}
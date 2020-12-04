package com.codehustlers.bankaks.utils

import android.app.Dialog
import android.content.Context
import com.codehustlers.bankaks.R

object SankalyaProgressUtil{
    private var progressDialog: Dialog? = null

    val isShowing: Boolean
        get() = progressDialog != null

    fun showOldProgressDialog(context: Context) {
        if (progressDialog == null || !progressDialog!!.isShowing) {
            progressDialog = Dialog(context, R.style.Theme_AppCompat_Dialog)
//            progressDialog!!.setContentView(R.layout.fragment_shopping_custom_progress_dialog)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.show()
            progressDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            progressDialog!!.setContentView(R.layout.layout_animated_progress_bar)
        }
    }

    fun forceShowOldProgressDialog(context: Context) {
        progressDialog = Dialog(context, R.style.Theme_AppCompat_Dialog)
//            progressDialog!!.setContentView(R.layout.fragment_shopping_custom_progress_dialog)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
        progressDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        progressDialog!!.setContentView(R.layout.layout_animated_progress_bar)
    }

    fun closeOldProgressDialog() {
        try {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
                progressDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            progressDialog = null
        }
    }

}
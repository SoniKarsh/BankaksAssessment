package com.codehustlers.bankaks.ui.optiondetails

import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.codehustlers.bankaks.R
import com.codehustlers.bankaks.data.ResTaskFormModel
import com.codehustlers.bankaks.utils.Constants
import com.codehustlers.bankaks.utils.ToastUtils
import com.google.gson.Gson
import org.json.JSONObject

class OptionDetailsViewModel: ViewModel() {

    var screenTitle = MutableLiveData<String>("")
    var fieldsData = MutableLiveData<List<ResTaskFormModel.Result.Field>>()
    var showProgressBar = MutableLiveData<Boolean>(false)
    var selectedPos = -1
    lateinit var hashMap: HashMap<Int, Int>

    // Call API for view Details
    fun callFormDetails(type: Int){

        // Initialize HashMap
        hashMap = HashMap()

        // Show Progress bar
        showProgressBar.postValue(true)
        AndroidNetworking.get(Constants.BASE_URL + Constants.TASK + type.toString())
            .setPriority(Priority.IMMEDIATE)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    showProgressBar.postValue(false)
                    val gsonObject = Gson()
                    // Json to Model
                    val resTaskFormModel = gsonObject.fromJson(response.toString(), ResTaskFormModel::class.java)
                    // Set screen Title
                    screenTitle.postValue(resTaskFormModel.result.screen_title)

                    // Send field data
                    fieldsData.postValue(resTaskFormModel.result.fields)
                }

                override fun onError(error: ANError) {
                    // Hide Progress bar
                    showProgressBar.postValue(false)
                    ToastUtils.shortToast(stringText = error.toString())
                }
            })
    }

    fun onItemSelectedListenerOptions(counter: Int): AdapterView.OnItemSelectedListener {

        return object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0){
                    if (parent?.getChildAt(0) != null && parent.getChildAt(0) is TextView) {
                        (parent.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(parent.context!!, R.color.colorGraySpinnerItem))
                    }
                } else {
                    if (parent?.getChildAt(0) != null && parent.getChildAt(0) is TextView) {
                        (parent.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(parent.context!!, android.R.color.black))
                    }
                }
                if(parent?.getChildAt(0) != null){
                    selectedPos = position
                    hashMap[counter] = selectedPos
                }
            }

        }

    }

}
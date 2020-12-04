package com.codehustlers.bankaks.ui.options

import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.codehustlers.bankaks.R
import com.codehustlers.bankaks.utils.SingleLiveEvent
import com.codehustlers.bankaks.utils.ToastUtils

class OptionsViewModel: ViewModel() {

    var optionPos = -1
    var navigateToDetail = SingleLiveEvent<Boolean>()

    init {
        navigateToDetail.postValue(false)
    }

    fun onClick(view: View){
        when(view.id){
            R.id.tvSendOption -> {
                if(optionPos == -1 || optionPos == 0){
                    ToastUtils.shortToast(stringText = view.context.getString(R.string.option_validation))
                } else {
                    navigateToDetail.postValue(true)
                }
            }
        }
    }

    fun onItemSelectedListenerOptions(): AdapterView.OnItemSelectedListener {

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
                    optionPos = position
                }
            }

        }

    }

}
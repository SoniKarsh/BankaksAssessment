package com.codehustlers.bankaks.ui.options

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.codehustlers.bankaks.R
import com.codehustlers.bankaks.databinding.FragmentOptionsBinding
import com.codehustlers.bankaks.utils.Constants
import com.codehustlers.bankaks.utils.KeyboardUtils
import com.codehustlers.bankaks.utils.NothingSelectedSpinnerAdapter

class OptionsFragment: Fragment() {

    private var optionsViewModel: OptionsViewModel? = null
    private lateinit var binding: FragmentOptionsBinding
    private lateinit var options: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        optionsViewModel = ViewModelProvider(this).get(OptionsViewModel::class.java)

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(container?.context)
            , R.layout.fragment_options, container, false) as FragmentOptionsBinding
        binding.lifecycleOwner = this
        binding.optionsViewModel = optionsViewModel

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        options = arrayListOf("Option 1", "Option 2", "Option 3")

        adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_options,
            R.id.tvOptionType,
            options
        )

        binding.acpOptions.adapter = NothingSelectedSpinnerAdapter(
            adapter,
            R.layout.item_options,
            requireContext()
        )

        binding.acpOptions.setSelection(0)

        hideKeyboardOnSpinner(binding.acpOptions)

        optionsViewModel?.navigateToDetail?.observe(viewLifecycleOwner, Observer {
            if(it){
                val position = optionsViewModel?.optionPos ?: 0
                val bundle = Bundle()
                bundle.putInt(Constants.OPTION_VALUE, position)
                Navigation.findNavController(binding.root)
                    .navigate(
                        R.id.action_optionsFragment_to_optionDetailsFragment,
                        bundle
                    )
            }
        })

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKeyboardOnSpinner(spinner: Spinner?) {
        spinner?.setOnTouchListener { v, event ->
            KeyboardUtils.hideKeyboard(view)
            false
        }
    }

}
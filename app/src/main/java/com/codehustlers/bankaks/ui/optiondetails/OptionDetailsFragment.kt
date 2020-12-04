package com.codehustlers.bankaks.ui.optiondetails

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.codehustlers.bankaks.R
import com.codehustlers.bankaks.data.ResTaskFormModel
import com.codehustlers.bankaks.databinding.FragmentOptionDetailsBinding
import com.codehustlers.bankaks.utils.NothingSelectedSpinnerAdapter
import com.codehustlers.bankaks.utils.*
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.roundToInt

class OptionDetailsFragment: Fragment() {

    private var optionDetailsViewModel: OptionDetailsViewModel? = null
    private lateinit var binding: FragmentOptionDetailsBinding
    private lateinit var listOfFields: ArrayList<ResTaskFormModel.Result.Field>
    private var counter = -1
    private lateinit var tempField: ResTaskFormModel.Result.Field
    private var isAllFieldValid = true

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        optionDetailsViewModel = ViewModelProvider(this).get(OptionDetailsViewModel::class.java)

        binding = DataBindingUtil.inflate(
                LayoutInflater.from(container?.context),
                R.layout.fragment_option_details,
                container,
                false
        ) as FragmentOptionDetailsBinding
        binding.lifecycleOwner = this
        binding.optionDetailsViewModel = optionDetailsViewModel

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Selected option for calling an API accordingly
        arguments?.getInt(Constants.OPTION_VALUE)?.let {
            optionDetailsViewModel?.callFormDetails(it)
        }

        // Show / Hide progress bar based on state of api calling.
        optionDetailsViewModel?.showProgressBar?.observe(viewLifecycleOwner, Observer {
            if (it) {
                SankalyaProgressUtil.showOldProgressDialog(requireContext())
            } else {
                SankalyaProgressUtil.closeOldProgressDialog()
            }
        })

        // Once api gives response with success status get data from api and generate UI dynamically
        optionDetailsViewModel?.fieldsData?.observe(viewLifecycleOwner, Observer {
            it.let {
                // if list of fields are null or empty then don't render UI.
                if(it.isNullOrEmpty()){
                    ToastUtils.shortToast(stringText = getString(R.string.no_fields_resp))
                } else {

                    listOfFields = it as ArrayList<ResTaskFormModel.Result.Field>

                    for (field in it) {
                        // Bases on UI type create Dynamic view and add it to the current root.
                        when (field.ui_type.type) {

                            // UI Type Dropdown / Spinner
                            Constants.UI_TYPE_DROPDOWN -> {

                                // Inflated View Of Spinner
                                val view = LayoutInflater.from(requireContext())
                                        .inflate(R.layout.layout_spinner, null)

                                // Take reference to Spinner
                                val spinner = view.findViewById<Spinner>(R.id.sLayoutItem)

                                // Set Adapter
                                val adapter = ArrayAdapter(
                                        requireContext(),
                                        R.layout.item_custom_spinner_text,
                                        R.id.tvSpinnerOptionType,
                                        field.ui_type.values.map {
                                            it.name
                                        }
                                )

                                // Nothing selected adapter for showing by default selected as Hint
                                // And not select on of the values
                                spinner.adapter = NothingSelectedSpinnerAdapter(
                                        adapter = adapter,
                                        hint = field.placeholder,
                                        nothingSelectedLayout = R.layout.item_custom_spinner_text,
                                        context = requireContext(),
                                        nothingSelectedDropdownLayout = R.layout.item_custom_spinner_text
                                )

                                // Counter to get count of views and used to maintain hashmap of selected position
                                // For each spinner so, can manage multiple spinners dynamically
                                counter++

                                // Set tag to use it later to validate
                                view.tag = counter

                                // On Item Selected for spinner Callback
                                spinner.onItemSelectedListener = optionDetailsViewModel?.onItemSelectedListenerOptions(counter)

                                // Add view to current root of the layout
                                binding.llRoot.addView(view)

                                // Spinner params used for giving spinner a specific height and margins to the view
                                val spinnerParams = spinner.layoutParams as LinearLayout.LayoutParams
                                spinnerParams.height = dpToPx(60)
                                spinner.layoutParams = spinnerParams

                                val layoutParams =
                                        view.layoutParams as LinearLayout.LayoutParams
                                layoutParams.setMargins(50, 50, 50, 0)

                                // Select 0th item by default which is hint used in nothing selected adapter
                                spinner.setSelection(0)

                                // Hide keyboard on spinner selection to maintain UI/UX
                                hideKeyboardOnSpinner(spinner)

                            }

                            // View TextField for form
                            Constants.UI_TYPE_TEXT_FIELD -> {

                                // Inflated View Of TextInputLayout For Form Field
                                val view = LayoutInflater.from(requireContext())
                                        .inflate(R.layout.layout_text_input_layout, null)

                                // Reference to TextInputLayout
                                val textInputLayout = view.findViewById<TextInputLayout>(R.id.tilWrap)

                                // Reference to TextInputEditText
                                val enterText = view.findViewById<TextInputEditText>(R.id.tietEnterText)
                                textInputLayout.hint = field.placeholder

                                // Check Data type
                                when(field.type.data_type){
                                    Constants.DATA_TYPE_INT -> {
                                        enterText.inputType = InputType.TYPE_CLASS_NUMBER
                                    }
                                    Constants.DATA_TYPE_STRING -> {
                                        enterText.inputType = InputType.TYPE_CLASS_TEXT
                                    }
                                }

                                // OnFocusChangedListener on EditText to set hint only when view is on focus otherwise
                                // Put blank
                                enterText.setOnFocusChangeListener { view, b ->
                                    if (b) {
                                        enterText.text?.isBlank().let {
                                            if(it == true){
                                                enterText.setText("")
                                            }
                                        }
                                        enterText.hint = field.hint_text
                                        KeyboardUtils.showKeyboard(requireActivity())
                                    } else {
                                        enterText.hint = ""
                                    }
                                }

                                // Set tag to use it later to validate
                                counter++
                                view.tag = counter

                                // Add View to root
                                binding.llRoot.addView(view)

                                // Give margin to created dynamic view
                                val layoutParams =
                                        view.layoutParams as LinearLayout.LayoutParams
                                layoutParams.setMargins(50, 50, 50, 0)
                            }
                        }
                    }

                    // Button -> On click of it check for validated data
                    val btnView = LayoutInflater.from(requireContext())
                            .inflate(R.layout.layout_button, null)

                    // Add View to root
                    binding.llRoot.addView(btnView)

                    // Button layout params
                    val btnLayoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    // Button gravity to center horizontal
                    btnLayoutParams.gravity = Gravity.CENTER_HORIZONTAL

                    // Button margin
                    btnLayoutParams.setMargins(0, 50, 0, 0)
                    btnView.layoutParams = btnLayoutParams

                    // On Click of validate Button
                    btnView.setOnClickListener {
                        // isAllFieldValid flag to validate field
                        isAllFieldValid = true

                        // Go through all visible views and validate dynamically
                        binding.llRoot.children.forEach {

                            // it.tag is a tag of each view as we have set integer to our views
                            // so, any tag which is not int isn't our concern
                            if(it.tag is Int){

                                // tempField to store field data in a temp variable
                                tempField = listOfFields[it.tag as Int]

                                // Check whether a given field is mandatory or not
                                when(tempField.is_mandatory){

                                    // If isMandatory
                                    Constants.TRUE -> {

                                        // UI Type Dropdown / TextField
                                        when(tempField.ui_type.type){

                                            // DROPDOWN
                                            Constants.UI_TYPE_DROPDOWN -> {

                                                // HashMap which contains selection information of all the Spinners
                                                optionDetailsViewModel?.hashMap.let { hashMap ->

                                                    // If HashMap value for given key is 0 / -1 / null then it means that the spinner value is not yet selected
                                                    // So, throw an error
                                                    if(hashMap?.get(it.tag as Int) != null
                                                            && hashMap[it.tag as Int] != -1
                                                            && hashMap[it.tag as Int] != 0) {

                                                        // get spinner reference and change its background from red or error color to normal
                                                        it.findViewById<Spinner>(R.id.sLayoutItem).background = ContextCompat.getDrawable(requireActivity(), R.drawable.bg_spinner)
                                                        // Remove error
                                                        it.findViewById<TextView>(R.id.tvSuOptionError).visibility = View.INVISIBLE
                                                    } else {

                                                        // Show error if spinner value isn't selected
                                                        it.findViewById<TextView>(R.id.tvSuOptionError).visibility = View.VISIBLE
                                                        it.findViewById<TextView>(R.id.tvSuOptionError).text = String.format(getString(R.string.error_enter_data), tempField.placeholder)

                                                        // Change spinner background to red to send alert message
                                                        it.findViewById<Spinner>(R.id.sLayoutItem).background = ContextCompat.getDrawable(requireActivity(), R.drawable.bg_spinner_error)
                                                        // Change text color to red which is in spinner as a hint
                                                        (it.findViewById<Spinner>(R.id.sLayoutItem).getChildAt(0) as TextView)
                                                                .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorErrorRed))
                                                        // isAllFieldValid = false as we found an error
                                                        isAllFieldValid = false
                                                    }
                                                }
                                            }

                                            // UI Type TextField
                                            Constants.UI_TYPE_TEXT_FIELD -> {

                                                // If text field is empty or null
                                                if(it.findViewById<TextInputEditText>(R.id.tietEnterText).text?.trim().isNullOrEmpty()){

                                                    // If empty show error
                                                    it.findViewById<TextInputLayout>(R.id.tilWrap).error = String.format(getString(R.string.error_enter_data), tempField.placeholder)
                                                    it.findViewById<TextInputLayout>(R.id.tilWrap).isErrorEnabled = true

                                                    // To give it a similar look as in material design
                                                    it.findViewById<TextInputEditText>(R.id.tietEnterText).setText(" ")

                                                    // isAllFieldValid = false as we found an error
                                                    isAllFieldValid = false
                                                }
                                                // If regex is not empty
                                                else if(tempField.regex.isNotEmpty()) {

                                                    // Check whether it satisfy the regex or not
                                                    val response = ValidationUtils.isFieldValid(tempField.regex, it.findViewById<TextInputEditText>(R.id.tietEnterText).text.toString())

                                                    // if response.first == true then not an error
                                                    if(response.first){
                                                        it.findViewById<TextInputLayout>(R.id.tilWrap).error = ""
                                                        it.findViewById<TextInputLayout>(R.id.tilWrap).isErrorEnabled = false
                                                    } else {
                                                        // isAllFieldValid = false as we found an error
                                                        isAllFieldValid = false
                                                        // Show Error
                                                        if(response.second == R.string.exception_occurred){
                                                            it.findViewById<TextInputLayout>(R.id.tilWrap).error = getString(R.string.exception_occurred)
                                                        } else {
                                                            it.findViewById<TextInputLayout>(R.id.tilWrap).error = String.format(getString(R.string.error_enter_valid_data), tempField.placeholder)
                                                        }
                                                        // To give it a similar look as in material design
                                                        // it.findViewById<TextInputEditText>(R.id.tietEnterText).setText(" ")
                                                        it.findViewById<TextInputLayout>(R.id.tilWrap).isErrorEnabled = true
                                                    }
                                                } else {
                                                    // No regex given
                                                    it.findViewById<TextInputLayout>(R.id.tilWrap).error = ""
                                                    it.findViewById<TextInputLayout>(R.id.tilWrap).isErrorEnabled = false
                                                }
                                            }
                                        }
                                    }

                                    Constants.FALSE -> {
                                        // Not mandatory so, no need to validate
                                    }

                                }
                            }
                        }

                        // If all fields are valid go ahead
                        if(isAllFieldValid){
                            ToastUtils.shortToast(stringText = getString(R.string.data_validated))
                        }

                    }

                }

            }
        })

    }

    // Dp to Pixel
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKeyboardOnSpinner(spinner: Spinner?) {
        spinner?.setOnTouchListener { v, event ->
            KeyboardUtils.hideKeyboard(view)
            false
        }
    }

}
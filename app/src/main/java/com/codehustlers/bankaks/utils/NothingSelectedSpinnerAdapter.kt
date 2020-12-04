package com.codehustlers.bankaks.utils

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * Decorator Adapter to allow a Spinner to show a 'Nothing Selected...' initially
 * displayed instead of the first choice in the Adapter.
 */
class NothingSelectedSpinnerAdapter
(private var adapter: SpinnerAdapter,
 private var nothingSelectedLayout: Int,
 private var nothingSelectedDropdownLayout: Int,
 private var context: Context, var hint: String = "", private val selectedTextColor: Int = 0) : SpinnerAdapter, ListAdapter {

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    constructor(
            spinnerAdapter: SpinnerAdapter,
            nothingSelectedLayout: Int, context: Context, hint: String = "", selectedTextColor: Int = 0) : this(spinnerAdapter, nothingSelectedLayout, -1, context, hint, selectedTextColor)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        return if (position == 0) {
            getNothingSelectedView(parent)
        } else {
            val view = adapter.getView(position - EXTRA, null, parent)
            if (view is TextView && selectedTextColor != 0) {
                try {
                    view.setTextColor(ContextCompat.getColor(context, selectedTextColor))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return view
        }
    }

    /**
     * View to show in Spinner with Nothing Selected
     * Override this to do something dynamic... e.g. "37 Options Found"
     * @param parent
     * @return
     */
    private fun getNothingSelectedView(parent: ViewGroup): View? {
        val view = layoutInflater.inflate(nothingSelectedLayout, parent, false)
        if (view is TextView && hint != "") {
            view.text = hint
        }
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        // Android BUG! http://code.google.com/p/android/issues/detail?id=17128 -
        // Spinner does not support multiple view types
        return if (position == 0) {
            if (nothingSelectedDropdownLayout == -1)
                View(context)
            else
                getNothingSelectedDropdownView(parent)
        } else adapter.getDropDownView(position - EXTRA, null, parent)

        // Could re-use the convertView if possible, use setTag...
    }

    /**
     * Override this to do something dynamic... For example, "Pick your favorite
     * of these 37".
     * @param parent
     * @return
     */
    private fun getNothingSelectedDropdownView(parent: ViewGroup): View {
        return layoutInflater.inflate(nothingSelectedDropdownLayout, parent, false)
    }

    override fun getCount(): Int {
        val count = adapter.count
        return if (count == 0) 0 else count + EXTRA
    }

    override fun getItem(position: Int): Any? {
        return if (position == 0) null else adapter.getItem(position - EXTRA)
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return if (position >= EXTRA) adapter.getItemId(position - EXTRA) else (position - EXTRA).toLong()
    }

    override fun hasStableIds(): Boolean {
        return adapter.hasStableIds()
    }

    override fun isEmpty(): Boolean {
        return adapter.isEmpty
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        adapter.registerDataSetObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        adapter.unregisterDataSetObserver(observer)
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0 // Don't allow the 'nothing selected'
        // item to be picked.
    }

    companion object {
        protected val EXTRA = 1
    }

}
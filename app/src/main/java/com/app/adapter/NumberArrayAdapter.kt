package com.app.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.ngn.R

class NumberArrayAdapter(
    context: Activity,
    resource: Int,
    textViewResourceId: Int,
    data: ArrayList<Int>
) : ArrayAdapter<Int>(context, resource, textViewResourceId, data) {
    private var inflater : LayoutInflater

    init {
        inflater = context.layoutInflater
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return rowView(convertView, parent, position)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return rowView(convertView, parent, position)
    }

    private fun rowView(convertView : View?, parent: ViewGroup , position: Int) : View {
        var currentView = convertView
        if(currentView == null){
            currentView = this.inflater.inflate(R.layout.com_text, parent, false)
        }

        currentView!!.apply {
            findViewById<TextView>(R.id.text).apply {
                text = getItem(position).toString()
            }
        }

        return currentView
    }
}
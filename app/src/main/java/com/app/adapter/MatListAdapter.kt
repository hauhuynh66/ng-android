package com.app.adapter

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import com.app.data.MatLine

class MatListAdapter(context : Context, private val resource: Int) : ArrayAdapter<MatLine>(context, resource) {


    class MatLineViewHolder(v : View){

    }
}
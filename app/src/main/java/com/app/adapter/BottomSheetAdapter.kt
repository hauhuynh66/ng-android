package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.dialog.OptionBottomSheet
import com.app.ngn.R

class BottomSheetAdapter(val context : Context, val data : ArrayList<OptionBottomSheet.BottomSheetData>, private val listeners : ArrayList<OptionBottomSheet.Listener>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ElementHolder((context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.com_sheet_element, parent, false
        ))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ElementHolder).bind(data[position], listeners[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ElementHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(data : OptionBottomSheet.BottomSheetData, listener : OptionBottomSheet.Listener){
            val text = v.findViewById<TextView>(R.id.com_sheet_element_display)
            text.text = data.display
            if(data.enable){
                text.setOnClickListener {
                    listener.onClick(data.option)
                }
            }
        }
    }
}
package com.note

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GenericNote(title : String, content : String) : Note(title, content) {
    override fun getViewHolder(): RecyclerView.ViewHolder {

    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v){

    }
}
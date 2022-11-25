package com.app.data.media

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListManager

class AudioManager(data : List<Audio>) : ListManager<Audio>(data){
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        )
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
    }

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v){
        fun bind(audio: Audio){
            itemView.findViewById<TextView>(android.R.id.text1).apply {
                text = audio.name
            }
        }
    }
}
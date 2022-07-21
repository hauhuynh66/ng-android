package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.AudioData
import com.app.ngn.R

class AudioListAdapter(val context : Context, val data : ArrayList<AudioData>, val type : Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return AudioInformationViewHolder(inflater.inflate(R.layout.com_text, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AudioInformationViewHolder).bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class AudioInformationViewHolder(val v: View) : RecyclerView.ViewHolder(v){
        fun bind(a : AudioData){
            val text = v.findViewById<TextView>(R.id.text)
            text.text = a.title
        }
    }
}
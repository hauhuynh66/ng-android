package com.app.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListManager
import com.app.ngn.R

class Message(val from : String? = null, val content : String? = null)

class MessageManager(private val data : List<Message>) : ListManager<Message> {
    private var lastPosition = -1
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_message, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
        setAnimation(holder.v, position)
    }

    override fun getSize(): Int {
        return data.size
    }

    inner class ViewHolder(val v : View) : RecyclerView.ViewHolder(v){
        fun bind(message : Message){
            val sender = v.findViewById<TextView>(R.id.name)
            val content = v.findViewById<TextView>(R.id.message)
            sender.text = message.from
            content.text = message.content
        }
    }

    private fun setAnimation(view : View, position : Int){
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(view.context, android.R.anim.slide_in_left)
            view.startAnimation(animation)
            lastPosition = position
        }
    }
}
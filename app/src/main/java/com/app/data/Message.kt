package com.app.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R
import java.util.*

class Message(val key: String? = null, val from : String? = null, val content : String? = null)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (from?.hashCode() ?: 0)
        result = 31 * result + (content?.hashCode() ?: 0)
        return result
    }
}

class MessageManager(data : List<Message>) : ListManager<Message>(data) {
    private var lastPosition = -1
    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.com_message, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(data[position])
        setAnimation(holder.v, position)
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
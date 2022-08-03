package com.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.Message
import com.app.ngn.R

class MessageAdapter(val context: Context, val list: List<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var lastPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return MessageHolder(inflater.inflate(R.layout.com_message, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MessageHolder).bind(list[position])
        setAnimation(holder.v, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as MessageHolder).clearAnimation()
    }

    class MessageHolder(val v : View) : RecyclerView.ViewHolder(v){
        fun bind(message : Message){
            val sender = v.findViewById<TextView>(R.id.name)
            val content = v.findViewById<TextView>(R.id.message)
            sender.text = message.from
            content.text = message.content
        }

        fun clearAnimation(){
            v.clearAnimation()
        }
    }

    private fun setAnimation(view : View, position : Int){
        if (position > lastPosition) {
            val animation: Animation =
                AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            view.startAnimation(animation)
            lastPosition = position
        }
    }
}
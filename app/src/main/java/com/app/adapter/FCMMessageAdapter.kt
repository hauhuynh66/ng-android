package com.app.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R
import com.google.firebase.messaging.RemoteMessage

class FCMMessageAdapter(val context : Context,val data: ArrayList<RemoteMessage>)
    : RecyclerView.Adapter<FCMMessageAdapter.FCMViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FCMViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return FCMViewHolder(inflater.inflate(R.layout.com_fcm_message, parent, false))
    }

    override fun onBindViewHolder(holder: FCMViewHolder, position: Int) {
        val message = holder.v.findViewById<TextView>(R.id.com_fmc_message)
        message.text = this.data[position].data.toString()
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class FCMViewHolder(val v:View): RecyclerView.ViewHolder(v){

    }
}
package com.app.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R

class BitmapAdapter(val context: Context,val data:ArrayList<String?>) : RecyclerView.Adapter<BitmapAdapter.BitmapViewHolder>() {
    var selected:String? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitmapViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return BitmapViewHolder(inflater.inflate(R.layout.com_image, parent, false))
    }

    override fun onBindViewHolder(holder: BitmapViewHolder, position: Int) {
        holder.bind(this.data[position], object : Callback{
            override fun onClick(path: String?) {
                selected = path
            }
        })
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class BitmapViewHolder(val v: View):RecyclerView.ViewHolder(v){
        fun bind(path:String?, callback: Callback){
            val image = v.findViewById<ImageView>(R.id.com_image_image)
            image.setImageBitmap(BitmapFactory.decodeFile(path))
            image.setOnClickListener{
                val bm = (image.drawable as BitmapDrawable).bitmap
                callback.onClick(path)
            }
        }
    }

    interface Callback{
        fun onClick(path:String?)
    }
}
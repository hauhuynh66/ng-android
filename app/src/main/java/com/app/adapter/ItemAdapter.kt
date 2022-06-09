package com.app.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.ItemData
import com.app.ngn.R
import com.app.task.ImageCallable
import com.app.task.TaskRunner

class ItemAdapter(val context: Activity,var data:ArrayList<ItemData>):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return ItemViewHolder(inflater.inflate(R.layout.com_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val name = holder.v.findViewById<TextView>(R.id.itemName)
        val price = holder.v.findViewById<TextView>(R.id.itemPrice)
        val image = holder.v.findViewById<ImageView>(R.id.itemImg)
        name.text = this.data[position].name
        price.text = this.data[position].price.toString()
        val runner = TaskRunner()
        runner.execute(ImageCallable(this.data[position].imgUrl), object : TaskRunner.Callback<Bitmap?>{
            override fun onComplete(result: Bitmap?) {
                image.setImageBitmap(result)
            }
        })
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    public class ItemViewHolder(val v: View):RecyclerView.ViewHolder(v){

    }
}
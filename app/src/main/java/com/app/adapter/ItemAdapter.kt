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

class ItemAdapter(val context: Activity,var data:ArrayList<ItemData>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            0->{
                HeaderViewHolder(inflater.inflate(R.layout.com_list_header, parent, false))
            }
            else->{
                ItemViewHolder(inflater.inflate(R.layout.com_item, parent, false))
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if(position==0){
            0
        }else{
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            0->{

            }
            else->{
                (holder as ItemViewHolder).bind(data[position-1])
            }
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class ItemViewHolder(val v: View):RecyclerView.ViewHolder(v){
        fun bind(itemData: ItemData){
            val name = v.findViewById<TextView>(R.id.itemName)
            val price = v.findViewById<TextView>(R.id.itemPrice)
            val image = v.findViewById<ImageView>(R.id.itemImg)
            name.text = itemData.name
            price.text = itemData.price.toString()
            val runner = TaskRunner()
            runner.execute(ImageCallable(itemData.imgUrl), object : TaskRunner.Callback<Bitmap?>{
                override fun onComplete(result: Bitmap?) {
                    image.setImageBitmap(result)
                }
            })
        }
    }

    class HeaderViewHolder(v:View):RecyclerView.ViewHolder(v)
}
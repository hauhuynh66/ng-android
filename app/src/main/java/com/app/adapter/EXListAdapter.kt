package com.app.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.data.FileData
import com.app.ngn.R
import com.app.util.Format.Companion.formatDate

class EXListAdapter(val context: Activity, val data: ArrayList<FileData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return when(viewType){
            0->{
                EXListFileViewHolder(inflater.inflate(R.layout.com_ex_list_file,parent, false))
            }
            else->{
                EXListFolderViewHolder(inflater.inflate(R.layout.com_ex_list_dir,parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            0->{
                (holder as EXListFileViewHolder).bind(data[position])
            }
            else->{
                (holder as EXListFolderViewHolder).bind(data[position])
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position].type){
            "DIR"->{
                1
            }
            else->{
                0
            }
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class EXListFolderViewHolder(private val v: View) : RecyclerView.ViewHolder (v){
        fun bind(fileData: FileData){
            v.setOnClickListener{
                fileData.listener.onClick(fileData.path)
            }

            v.setOnLongClickListener {
                fileData.listener.onLongClick(fileData.path)
                true
            }

            val button = v.findViewById<ImageButton>(R.id.com_ex_list_next_btn)
            button.setOnClickListener {
                fileData.listener.onClick(fileData.path)
            }

            val name = v.findViewById<TextView>(R.id.com_ex_list_name)
            val size = v.findViewById<TextView>(R.id.com_ex_list_size)
            val createdDate = v.findViewById<TextView>(R.id.com_ex_list_date)
            name.text = fileData.name

            if(fileData.size!=null){
                size.text = fileData.size.toString()
            }

            createdDate.text = formatDate(fileData.createDate)
        }
    }

    class EXListFileViewHolder(private val v:View) : RecyclerView.ViewHolder(v){
        fun bind(fileData: FileData){
            v.setOnClickListener{
                fileData.listener.onClick(fileData.path)
            }

            v.setOnLongClickListener {
                fileData.listener.onLongClick(fileData.path)
                true
            }

            val name = v.findViewById<TextView>(R.id.com_ex_list_name)
            val size = v.findViewById<TextView>(R.id.com_ex_list_size)
            val createdDate = v.findViewById<TextView>(R.id.com_ex_list_date)
            name.text = fileData.name
            if(fileData.size!=null){
                size.text = fileData.size.toString()
            }
            createdDate.text = formatDate(fileData.createDate)
        }
    }
}
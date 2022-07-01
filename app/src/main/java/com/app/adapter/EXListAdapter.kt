package com.app.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.app.data.FileData
import com.app.ngn.R
import com.app.util.Format.Companion.formatDate

class EXListAdapter(val context: Activity, var data: ArrayList<FileData>,
                    private val isGrid: Boolean, val listener: Listener? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return if(!isGrid){
            when(viewType){
                0->{
                    EXListFileViewHolder(inflater.inflate(R.layout.com_ex_list_file,parent, false))
                }
                else->{
                    EXListFolderViewHolder(inflater.inflate(R.layout.com_ex_list_dir,parent, false))
                }
            }
        }else{
            EXGridFileViewHolder(inflater.inflate(R.layout.com_ex_grid,parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(isGrid){
            (holder as EXGridFileViewHolder).bind(data[position])
        }else {
            when (holder.itemViewType) {
                0 -> {
                    (holder as EXListFileViewHolder).bind(data[position], listener!!, position)
                }
                else -> {
                    (holder as EXListFolderViewHolder).bind(data[position], listener!!, position)
                }
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
        fun bind(fileData: FileData, listener: Listener, position : Int){
            val chk = v.findViewById<CheckBox>(R.id.com_ex_list_check)
            chk.isChecked = fileData.checked
            chk.setOnClickListener {
                if(chk.isChecked){
                    listener.onCheck(fileData.path)
                }else{
                    listener.onUnCheck(fileData.path)
                }
                fileData.checked = chk.isChecked
            }

            val next = v.findViewById<ConstraintLayout>(R.id.com_ex_list_info_group)
            next.setOnClickListener{
                listener.onClick(fileData.path, chk.isChecked, position)
            }

            next.setOnLongClickListener {
                listener.onLongClick(fileData.path)
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

    class EXListFileViewHolder(private val v:View) : RecyclerView.ViewHolder(v){
        fun bind(fileData: FileData, listener: Listener, position : Int){
            val chk = v.findViewById<CheckBox>(R.id.com_ex_list_check)
            chk.isChecked = fileData.checked
            chk.setOnClickListener {
                if(chk.isChecked){
                    listener.onCheck(fileData.path)
                }else{
                    listener.onUnCheck(fileData.path)
                }
                fileData.checked = chk.isChecked
            }

            val next = v.findViewById<ConstraintLayout>(R.id.com_ex_list_info_group)
            next.setOnClickListener{
                listener.onClick(fileData.path, chk.isChecked, position)
            }

            next.setOnLongClickListener {
                listener.onLongClick(fileData.path)
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

    class EXGridFileViewHolder(private val v:View) : RecyclerView.ViewHolder(v){
        fun bind(fileData: FileData){
            val icon = v.findViewById<ImageView>(R.id.com_ex_grid_icon)
            val name = v.findViewById<TextView>(R.id.com_ex_grid_name)
            name.text = fileData.name.substringBeforeLast(".")
        }
    }

    interface Listener{
        fun onCheck(path: String)
        fun onUnCheck(path: String)
        fun onClick(path : String, isChecked: Boolean, position: Int)
        fun onLongClick(path : String)
    }
}
package com.app.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.data.FileData
import com.app.ngn.R
import com.app.util.Formatter.Companion.formatDate

class ExplorerListAdapter(val context: Context, var data: ArrayList<FileData>,
                          var isGrid: Boolean, val listener: Listener? = null, var isMultiple: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return if(!isGrid){
            when(viewType){
                0->{
                    EXListFileViewHolder(inflater.inflate(R.layout.com_item_holder,parent, false))
                }
                else->{
                    EXListFolderViewHolder(inflater.inflate(R.layout.com_item_holder,parent, false))
                }
            }
        }else{
            EXGridFileViewHolder(inflater.inflate(R.layout.com_ex_grid,parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(isGrid){
            (holder as EXGridFileViewHolder).bind(data[position], listener!!, position, context, isMultiple)
        }else {
            when (holder.itemViewType) {
                0 -> {
                    (holder as EXListFileViewHolder).bind(data[position], listener!!, position, isMultiple)
                }
                else -> {
                    (holder as EXListFolderViewHolder).bind(data[position], listener!!, position, isMultiple)
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
        fun bind(fileData: FileData, listener: Listener, position : Int, isMultiple: Boolean){
            val chk = v.findViewById<CheckBox>(R.id.checkbox)
            val chkGroup = v.findViewById<ConstraintLayout>(R.id.checkbox_group)
            chk.isChecked = fileData.checked
            chk.setOnClickListener {
                if(chk.isChecked){
                    listener.onCheck(fileData.path)
                }else{
                    listener.onUnCheck(fileData.path)
                }
                fileData.checked = chk.isChecked
            }

            chkGroup.visibility = if(isMultiple) View.VISIBLE else View.GONE

            val next = v.findViewById<ConstraintLayout>(R.id.info_group)
            next.setOnClickListener{
                listener.onClick(fileData.path, chk.isChecked, position)
            }

            next.setOnLongClickListener {
                listener.onLongClick(fileData.path)
                true
            }

            v.findViewById<TextView>(R.id.title).apply {
                text = fileData.name
            }

            v.findViewById<TextView>(R.id.description2).apply {
                if(fileData.size!=null){
                    text = fileData.size.toString()
                }
            }

            v.findViewById<TextView>(R.id.description1).apply {
                text = formatDate(fileData.createDate, "yyyy/MM/dd HH:mm")
            }

            v.findViewById<ImageView>(R.id.icon).apply {
                setImageResource(R.drawable.ic_baseline_folder)
            }


        }
    }

    class EXListFileViewHolder(private val v:View) : RecyclerView.ViewHolder(v){
        fun bind(fileData: FileData, listener: Listener, position : Int, isMultiple: Boolean){
            val chk = v.findViewById<CheckBox>(R.id.checkbox)
            val chkGroup = v.findViewById<ConstraintLayout>(R.id.checkbox_group)
            chk.isChecked = fileData.checked
            chk.setOnClickListener {
                if(chk.isChecked){
                    listener.onCheck(fileData.path)
                }else{
                    listener.onUnCheck(fileData.path)
                }
                fileData.checked = chk.isChecked
            }

            chkGroup.visibility = if(isMultiple) View.VISIBLE else View.GONE

            val next = v.findViewById<ConstraintLayout>(R.id.info_group)
            next.setOnClickListener{
                listener.onClick(fileData.path, chk.isChecked, position)
            }

            next.setOnLongClickListener {
                listener.onLongClick(fileData.path)
                true
            }

            v.findViewById<TextView>(R.id.title).apply {
                text = fileData.name
            }
            v.findViewById<TextView>(R.id.description2).apply {
                if(fileData.size!=null){
                    text = fileData.size.toString()
                }
            }
            v.findViewById<TextView>(R.id.description1).apply {
                text = formatDate(fileData.createDate, "yyyy/MM/dd HH:mm")
            }

            v.findViewById<ImageView>(R.id.icon).apply {
                setImageResource(R.drawable.ic_baseline_description)
            }
        }
    }

    class EXGridFileViewHolder(private val v:View) : RecyclerView.ViewHolder(v){
        fun bind(fileData: FileData, listener: Listener, position: Int, context: Context, isMultiple: Boolean){
            val icon = v.findViewById<ImageView>(R.id.com_ex_grid_icon)
            val name = v.findViewById<TextView>(R.id.com_ex_grid_name)
            val chk = v.findViewById<CheckBox>(R.id.com_ex_grid_check)
            chk.isChecked = fileData.checked
            chk.setOnClickListener {
                if(chk.isChecked){
                    listener.onCheck(fileData.path)
                }else{
                    listener.onUnCheck(fileData.path)
                }
                fileData.checked = chk.isChecked
            }

            chk.visibility = if(isMultiple) View.VISIBLE else View.GONE

            name.text = fileData.name
            val drawable: Drawable?
            if(fileData.type=="DIR"){
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_folder)
                v.setOnClickListener {
                    listener.onClick(fileData.path, chk.isChecked, position)
                }
                name.setTextSize(TypedValue.COMPLEX_UNIT_PT, 6f)
            }else{
                drawable = ContextCompat.getDrawable(context, R.drawable.ic_baseline_description)
                name.setTextSize(TypedValue.COMPLEX_UNIT_PT, 4f)
            }
            v.setOnLongClickListener {
                listener.onLongClick(fileData.path)
                true
            }
            icon.setImageDrawable(drawable)
        }
    }

    interface Listener{
        fun onCheck(path: String)
        fun onUnCheck(path: String)
        fun onClick(path : String, isChecked: Boolean, position: Int)
        fun onLongClick(path : String)
    }
}
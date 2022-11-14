package com.explorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.ngn.R
import com.app.util.Formatter.Companion.formatDate

class ExplorerListAdapter(fileInfoList : List<FileInfo>? = null , var isGrid: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data: MutableList<FileDisplay> = mutableListOf()
    private var currentMode : Mode = Mode.Display
    private var clickListener : OnItemClickListener = object : OnItemClickListener {}
    private var longClickListener : OnItemLongClickListener = object : OnItemLongClickListener {}

    enum class Mode{
        Display,
        Select
    }

    init {
        fileInfoList?.forEach {
            data.add(FileDisplay(it))
        }
    }

    fun setData(fileInfoList: List<FileInfo>?){
        data.clear()
        fileInfoList?.forEach {
            data.add(FileDisplay(it))
        }
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.clickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener){
        this.longClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if(!isGrid){
            LineViewHolder(inflater.inflate(R.layout.com_ex_line,parent, false))
        }else{
            GridViewHolder(inflater.inflate(R.layout.com_ex_grid,parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(isGrid){
            (holder as GridViewHolder).bind(
                data[position],
                currentMode,
                clickListener,
                longClickListener
            )
        }else {
            (holder as LineViewHolder).bind(
                data[position],
                currentMode,
                clickListener,
                longClickListener
            )
        }

    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class LineViewHolder(val v: View) : RecyclerView.ViewHolder (v){
        fun bind(
            data: FileDisplay, mode: Mode,
            clickListener: OnItemClickListener, longClickListener: OnItemLongClickListener
        ){
            val checkBox = itemView.findViewById<CheckBox>(R.id.checkbox)
            checkBox.apply {
                visibility = if(mode == Mode.Select) View.VISIBLE else View.GONE
                isChecked = data.checked
                setOnClickListener {
                    checkBox.isSoundEffectsEnabled = true
                    data.checked = isChecked
                }
            }

            itemView.setOnClickListener{
                if(mode == Mode.Display){
                    clickListener.onClick(data.info)
                }else{
                    checkBox.isSoundEffectsEnabled = false
                    checkBox.performClick()
                }
            }

            itemView.setOnLongClickListener {
                longClickListener.onLongClick(data.info)
                true
            }

            v.findViewById<TextView>(R.id.title).apply {
                text = data.info.name
            }

            v.findViewById<TextView>(R.id.description2).apply {
                text = if(FileTable.fromExtension(data.info.extension)==FileTable.DIRECTORY){
                    ""
                }else{
                    data.info.size.toString()
                }
            }

            v.findViewById<TextView>(R.id.description1).apply {
                text = formatDate(data.info.createDate, "yyyy/MM/dd HH:mm")
            }

            v.findViewById<ImageView>(R.id.icon).apply {
                val res = when(FileTable.fromExtension(data.info.extension)){
                    FileTable.DIRECTORY->{
                        ContextCompat.getDrawable(context, R.drawable.ic_baseline_folder)
                    }
                    else->{
                        ContextCompat.getDrawable(context, R.drawable.ic_baseline_description)
                    }
                }
                setImageDrawable(res)
            }
        }
    }

    class GridViewHolder(private val v:View) : RecyclerView.ViewHolder(v){
        fun bind(
            data: FileDisplay, mode: Mode,
            clickListener: OnItemClickListener, longClickListener: OnItemLongClickListener
        ){
            val name = v.findViewById<TextView>(R.id.com_ex_grid_name)
            val chk = v.findViewById<CheckBox>(R.id.com_ex_grid_check)
            chk.isChecked = data.checked
            chk.setOnClickListener {
                data.checked = !data.checked
            }

            chk.visibility = if(mode == Mode.Select) View.VISIBLE else View.GONE

            name.text = data.info.name

            v.setOnClickListener {
                if(mode == Mode.Display){
                    clickListener.onClick(data.info)
                }else{
                    chk.isSoundEffectsEnabled = false
                    chk.performClick()
                }
            }

            v.setOnLongClickListener {
                longClickListener.onLongClick(data.info)
                true
            }
            v.findViewById<ImageView>(R.id.com_ex_grid_icon).apply {
                val res = when(FileTable.fromExtension(data.info.extension)){
                    FileTable.DIRECTORY->{
                        ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_folder)
                    }
                    else->{
                        ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_description)
                    }
                }
                setImageDrawable(res)
            }
        }
    }

    fun changeMode(mode : Mode){
        this.currentMode = mode
        data.forEach{
            it.checked  = false
        }
        notifyDataSetChanged()
    }

    fun select(position: Int){
        if(currentMode == Mode.Select){
            data[position].checked = !data[position].checked
            notifyItemChanged(position)
        }
    }

    fun flip(){
        val s = data.filter {
            it.checked
        }.size

        if(s < data.size){
            data.filter {
                !it.checked
            }.forEach {
                it.checked = true
            }
        }else{
            data.forEach {
                it.checked = !it.checked
            }
        }
        notifyDataSetChanged()
    }

    fun getSelected() : List<FileDisplay>{
        return data.filter {
            it.checked
        }
    }

    interface OnItemClickListener{
        fun onClick(fileInfo : FileInfo){

        }
    }

    interface OnItemLongClickListener{
        fun onLongClick(fileInfo : FileInfo){

        }
    }
}
package com.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.data.FileDisplay
import com.app.data.FileType
import com.app.ngn.R
import com.app.util.Formatter.Companion.formatDate
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

class ExplorerListAdapter(var root : String, var isGrid: Boolean, var mode: Mode = Mode.Display) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: MutableList<FileDisplay> = arrayListOf()
    private var clickListener : OnItemClickListener = object : OnItemClickListener{}
    private var longClickListener : OnItemLongClickListener = object : OnItemLongClickListener{}

    enum class Mode{
        Display,
        Select
    }

    init {
        data = getFileList(this.root)
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
            LineViewHolder(inflater.inflate(R.layout.com_item,parent, false))
        }else{
            GridViewHolder(inflater.inflate(R.layout.com_ex_grid,parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(isGrid){
            (holder as GridViewHolder).bind(data[position], position, mode, clickListener, longClickListener)
        }else {
            (holder as LineViewHolder).bind(data[position], position, mode, clickListener, longClickListener)
        }

    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    class LineViewHolder(val v: View) : RecyclerView.ViewHolder (v){
        fun bind(data: FileDisplay, position : Int, mode: Mode,
                 clickListener: OnItemClickListener, longClickListener: OnItemLongClickListener){
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
                    clickListener.onClick(position)
                }else{
                    checkBox.isSoundEffectsEnabled = false
                    checkBox.performClick()
                }

            }

            itemView.setOnLongClickListener {
                longClickListener.onLongClick(position)
                true
            }

            v.findViewById<TextView>(R.id.title).apply {
                text = data.name
            }

            v.findViewById<TextView>(R.id.description2).apply {
                text = if(data.type != FileType.DIRECTORY){
                    data.size?.toString()
                }else{
                    ""
                }
            }

            v.findViewById<TextView>(R.id.description1).apply {
                text = formatDate(data.createDate, "yyyy/MM/dd HH:mm")
            }

            v.findViewById<ImageView>(R.id.icon).apply {
                val res = when(data.type){
                    FileType.DIRECTORY->{
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
        fun bind(data: FileDisplay, position: Int, mode: Mode,
                 clickListener: OnItemClickListener, longClickListener: OnItemLongClickListener){
            val name = v.findViewById<TextView>(R.id.com_ex_grid_name)
            val chk = v.findViewById<CheckBox>(R.id.com_ex_grid_check)
            chk.isChecked = data.checked
            chk.setOnClickListener {
                data.checked = !data.checked
            }

            chk.visibility = if(mode == Mode.Select) View.VISIBLE else View.GONE

            name.text = data.name

            v.setOnClickListener {
                if(mode == Mode.Display){
                    clickListener.onClick(position)
                }else{
                    chk.isSoundEffectsEnabled = false
                    chk.performClick()
                }
            }

            v.setOnLongClickListener {
                longClickListener.onLongClick(position)
                true
            }
            v.findViewById<ImageView>(R.id.com_ex_grid_icon).apply {
                val res = when(data.type){
                    FileType.DIRECTORY->{
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
        this.mode = mode
        data.forEach{
            it.checked  = false
        }
        notifyDataSetChanged()
    }

    fun select(position: Int){
        if(mode == Mode.Select){
            data[position].checked = !data[position].checked
            notifyItemChanged(position)
        }
    }

    fun setPath(root: String){
        this.root = root
        data = getFileList(this.root)
        notifyDataSetChanged()
    }

    fun back(currentPath : String) : String{
        val parent = Path(currentPath).parent
        setPath(parent.absolutePathString())
        return parent.absolutePathString()
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

    fun refresh(){
        this.data = getFileList(root)
        notifyDataSetChanged()
    }

    fun getAction(position: Int) : Pair<String, String>{
        var action = "open"
        if(data[position].type == FileType.DIRECTORY){
            action = "next"
        }
        return Pair(action, data[position].absolutePath)
    }

    private fun getFileList(path : String) : ArrayList<FileDisplay> {
        val ret = arrayListOf<FileDisplay>()
        val file = File(path)
        if(file.isDirectory && file.listFiles()!=null){
            for(f: File? in file.listFiles()){
                if(file.exists()){
                    f!!.apply {
                        val attrs = Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)
                        val date = attrs.creationTime().toMillis()
                        if(f.isDirectory){
                            ret.add(FileDisplay(f.name, Date(date), null, f.absolutePath, FileType.DIRECTORY))
                        }else{
                            ret.add(FileDisplay(f.name, Date(date), f.length()/1024, f.absolutePath, FileType.fromExtension(f.extension)))
                        }
                    }
                }
            }
        }
        return ret
    }

    interface OnItemClickListener{
        fun onClick(position: Int){

        }
    }

    interface OnItemLongClickListener{
        fun onLongClick(position: Int){

        }
    }
}
package com.explorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.adapter.ListManager
import com.app.ngn.R
import java.io.File
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.io.path.Path

data class FileInfo(
    var name : String,
    val createDate: Date,
    val size: Long?,
    val absolutePath: String,
    val extension: String?
){
    companion object{
        fun fromFile(file : File) : FileInfo{
            if(!file.exists()){
                throw IllegalArgumentException("File not exist")
            }else{
                val attrs = Files.readAttributes(file.toPath(), BasicFileAttributes::class.java)
                val date = Date(attrs.creationTime().toMillis())
                var extension: String? = null
                var size : Long? = null
                if(!file.isDirectory){
                    extension = file.extension
                    size = Files.size(Path(file.absolutePath)) / 1024L
                }
                return FileInfo(file.name, date, size, file.absolutePath, extension)
            }
        }
    }
}

enum class FileTable{
    DIRECTORY,
    AUDIO,
    VIDEO,
    IMAGE,
    EXCEL,
    WORD,
    PDF,
    FILE;
    companion object{
        fun fromExtension(extension: String?) : FileTable {
            return if (extension == null) {
                return DIRECTORY
            } else {
                when (extension) {
                    null->{
                        DIRECTORY
                    }
                    "mp3", "wav"->{
                        AUDIO
                    }
                    "mp4", "flv"->{
                        VIDEO
                    }
                    "jpg", "png", "jpeg"->{
                        IMAGE
                    }
                    "xls", "xlsx"->{
                        EXCEL
                    }
                    "doc", "docx"->{
                        WORD
                    }
                    "pdf"->{
                        PDF
                    }
                    else -> {
                        FILE
                    }
                }
            }
        }

        fun getResource(fileTable: FileTable) : Int{
            return when(fileTable){
                DIRECTORY->{
                    R.drawable.ic_baseline_folder
                }
                else->{
                    R.drawable.ic_baseline_description
                }
            }
        }
    }
}

data class FileDisplay(
    val info : FileInfo,
    var checked : Boolean = false,
    var disabled : Boolean = false)


class FileInfoManager(private val data : List<FileInfo>, private val type: Type) : ListManager<FileInfo>{
    private val displayData : MutableList<FileDisplay> = mutableListOf()
    private var currentItemMode : ItemMode = ItemMode.Select

    enum class Type{
        Grid,
        Line
    }

    enum class ItemMode{
        Select,
        Display
    }

    init {
        data.forEach {
            displayData.add(FileDisplay(it))
        }
    }

    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(type){
            Type.Grid->{
                GridView(inflater.inflate(R.layout.com_ex_grid, parent, false))
            }
            Type.Line->{
                LineView(inflater.inflate(R.layout.com_ex_line, parent, false))
            }
        }
    }

    override fun bind(holder: RecyclerView.ViewHolder, position: Int) {
        when(type){
            Type.Grid->{
                (holder as GridView).bind()
            }
            Type.Line->{
                (holder as LineView).bind()
            }
        }
    }

    override fun getSize(): Int {
        return data.size
    }

    inner class LineView(v : View) : RecyclerView.ViewHolder(v) {
        fun bind(){

        }
    }

    inner class GridView(v : View) : RecyclerView.ViewHolder(v) {
        fun bind(){

        }
    }
}

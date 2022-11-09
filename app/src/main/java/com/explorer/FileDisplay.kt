package com.explorer

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
    FILE;

    companion object{
        fun fromExtension(extension: String?) : FileTable{
            return if(extension == null){
                return DIRECTORY
            }
            else{
                when(extension){
                    else->{
                        FILE
                    }
                }
            }
        }
    }
}

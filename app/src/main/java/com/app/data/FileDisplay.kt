package com.app.data

import java.util.*

class FileDisplay(
    var name : String,
    val createDate: Date,
    val size: Long?,
    val absolutePath: String,
    val extension: String?,
    var checked : Boolean = false
)

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

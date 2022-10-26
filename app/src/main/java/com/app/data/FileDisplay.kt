package com.app.data

import java.util.*

class FileDisplay(
    var name : String,
    val createDate: Date,
    val size: Long?,
    val absolutePath: String,
    val type: FileType,
    var checked : Boolean = false){
}

enum class FileType{
    DIRECTORY,
    IMAGE,
    VIDEO,
    AUDIO,
    OTHERS;

    companion object{
        fun fromExtension(extension : String): FileType {
            return when(extension){
                "jpg"->{
                    IMAGE
                }
                "mp3"->{
                    AUDIO
                }
                "mp4"->{
                    VIDEO
                }
                else->{
                    OTHERS
                }
            }
        }
    }
}


package com.app.data.explorer

import com.app.ngn.R

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
                DIRECTORY ->{
                    R.drawable.ic_baseline_folder
                }
                else->{
                    R.drawable.ic_baseline_description
                }
            }
        }
    }
}
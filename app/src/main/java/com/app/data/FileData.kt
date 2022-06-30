package com.app.data

import java.util.*

class FileData(
    var name : String, val createDate: Date,
    val size: Long?, val listener : Listener,
    val path: String, val type: String, var checked : Boolean = false) {
    interface Listener{
        fun onClick(path : String)
        fun onLongClick(path : String)
    }
}
package com.app.data

import java.util.*

class FileData(val name : String, val createDate: Date, val size: Long, val listener : Listener, val path: String) {
    interface Listener{
        fun onClick(path : String)
    }
}
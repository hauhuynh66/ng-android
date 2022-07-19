package com.app.data

import java.util.*

class FileData(
    var name : String,
    val createDate: Date,
    val size: Long?,
    val path: String,
    val type: String,
    var checked : Boolean = false)
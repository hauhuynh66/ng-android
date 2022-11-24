package com.app.data.explorer

data class FileDisplay(
    val info : FileInfo,
    var checked : Boolean = false,
    var disabled : Boolean = false)
